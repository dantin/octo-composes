package com.github.dantin.webster.common.uid.buffer;

import com.github.dantin.webster.common.uid.utils.PaddedAtomicLong;
import com.google.common.base.MoreObjects;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RingBuffer is a data structure based on array.
 *
 * <p>Using array could improve read element performance due to the CPU cache line. To prevent the
 * side effect of False Sharing, {@link PaddedAtomicLong} is using on 'tail' and 'cursor'.
 *
 * <p>A ring buffer is consisted of:
 * <li><b>slots:</b> each element of the array is a slot, which is be set with a UID
 * <li><b>flags:</b> flag array corresponding the same index with the slots, indicates whether can
 *     take or put slot
 * <li><b>tail:</b> a sequence of the max slot position to produce
 * <li><b>cursor:</b> a sequence of the min slot position to consume
 */
public class RingBuffer {

  public static final int DEFAULT_PADDING_PERCENT = 50;
  private static final Logger LOGGER = LoggerFactory.getLogger(RingBuffer.class);
  /* Constants */
  private static final int START_POINT = -1;
  private static final long CAN_PUT_FLAG = 0L;
  private static final long CAN_TAKE_FLAG = 1L;
  /* The size of RingBuffer's slots, each slot hold a UID */
  private final int bufferSize;
  private final long indexMask;
  private final long[] slots;
  private final PaddedAtomicLong[] flags;

  /* Tail: last position sequence to produce */
  private final AtomicLong tail = new PaddedAtomicLong(START_POINT);

  /* Cursor: current position sequence to consume */
  private final AtomicLong cursor = new PaddedAtomicLong(START_POINT);

  /* Threshold for trigger padding buffer */
  private final int paddingThreshold;

  /* Reject put/take buffer handle policy */
  private RejectedPutBufferHandler rejectedPutHandler = this::discardPutBuffer;
  private RejectedTakeBufferHandler rejectedTakeHandler = this::exceptionRejectedTakeBuffer;

  /* Executor of padding buffer */
  private BufferPaddingExecutor bufferPaddingExecutor;

  /**
   * Constructor using buffer size, paddingFactor default as {@value #DEFAULT_PADDING_PERCENT}.
   *
   * @param bufferSize must be positive and a power of 2
   */
  public RingBuffer(int bufferSize) {
    this(bufferSize, DEFAULT_PADDING_PERCENT);
  }

  /**
   * Constructor with buffer size and padding factor.
   *
   * @param bufferSize must be positive and a power of 2
   * @param paddingFactor percent in (0, 100). When the count of rest available UIDs reach the
   *     threshold, it will trigger padding buffer. E.g., paddingFactor=20, bufferSize=1000 ==>
   *     threshold=1000*20/100, padding buffer will be triggered when tail-cursor < threshold
   */
  public RingBuffer(int bufferSize, int paddingFactor) {
    if (bufferSize <= 0L) {
      throw new RuntimeException("RingBuffer size must be positive");
    }
    if (Integer.bitCount(bufferSize) != 1) {
      throw new RuntimeException("RingBuffer size must be a power of 2");
    }
    if (paddingFactor <= 0 || paddingFactor > 100) {
      throw new RuntimeException("RingBuffer size must between 0 to 100");
    }
    this.bufferSize = bufferSize;
    this.indexMask = bufferSize - 1;
    this.slots = new long[bufferSize];
    this.flags = initFlags(bufferSize);

    this.paddingThreshold = bufferSize * paddingFactor / 100;
  }

  /**
   * Put an UID in the ring and move tail. Use {@code synchronized} to guarantee the UID fill in
   * slot and publish new tail sequence as atomic operations.
   *
   * <p><b>NOTE</b>: It is recommended to put UID in a serialize way, cause we once batch generate a
   * series UIDs and put the one by one into the buffer, so it is unnecessary put in multi-thread.
   *
   * @param uid UID
   * @return false if the buffer is full, true otherwise
   */
  public synchronized boolean put(long uid) {
    long currentTail = tail.get();
    long currentCursor = cursor.get();

    // tail catch the cursor, means that RingBuffer is full, so you can't put any.
    long distance = currentTail - (currentCursor == START_POINT ? 0 : currentCursor);
    if (distance == bufferSize - 1) {
      rejectedPutHandler.rejectPutBuffer(this, uid);
      return false;
    }

    // 1. pre-check whether the flag is CAN_PUT_FLAG.
    int nextTailIndex = calSlotIndex(currentTail + 1);
    if (flags[nextTailIndex].get() != CAN_PUT_FLAG) {
      rejectedPutHandler.rejectPutBuffer(this, uid);
      return false;
    }

    // 2. put UID in the next slot
    slots[nextTailIndex] = uid;
    // 3. update next slot's flag to CAN_TAKE_FLAG
    flags[nextTailIndex].set(CAN_TAKE_FLAG);
    // 4. publish tail with sequence increase by one
    tail.incrementAndGet();

    // The atomicity of operation above, guarantees by 'synchronized'.  In another word, the take
    // operation can't
    // consume the UID we just put, until the tail is published.
    return true;
  }

  /**
   * Take an UID of the ring at the next cursor, this is a lock free operation by using atomic
   * cursor.
   *
   * <p>Before getting the UID, we also check whether reach the padding threshold, the padding
   * buffer operation will be trigger in another thread. If there is no more available UID to be
   * taken, the specified {@link RejectedTakeBufferHandler} will be applied.
   *
   * @return UID
   */
  public long take() {
    // spin get next available cursor
    long currentCursor = cursor.get();
    long nextCursor = cursor.updateAndGet(old -> old == tail.get() ? old : old + 1);

    // check for safety consideration, it never happens
    if (nextCursor < currentCursor) {
      throw new RuntimeException("cursor can't move back");
    }

    // trigger padding in an async-mode if reach the threshold
    long currentTail = tail.get();
    if (currentTail - nextCursor < paddingThreshold) {
      LOGGER.info(
          "reach the padding threshold: {}. tail: {}, cursor: {}, rest: {}",
          paddingThreshold,
          currentTail,
          nextCursor,
          currentTail - nextCursor);
      bufferPaddingExecutor.asyncPadding();
    }

    // cursor catch the tail, means that there is no more available UID to take
    if (nextCursor == currentCursor) {
      rejectedTakeHandler.rejectTakeBuffer(this);
    }

    // 1. check next slot flag is CAN_TAKE_FLAG
    int nextCursorIndex = calSlotIndex(nextCursor);
    if (flags[nextCursorIndex].get() != CAN_TAKE_FLAG) {
      throw new RuntimeException("cursor not in can take status");
    }

    // 2. get UID from next slot
    long uid = slots[nextCursorIndex];
    // 3. set next slot flag as CAN_PUT_FLAG
    flags[nextCursorIndex].set(CAN_PUT_FLAG);

    // NOTE: step 2, 3 can not swap.  If we set flag before get value of slot, the producer may
    // overwrite the slot
    // with a new UID, and this my cause the consumer take the UID twice after walk a round the
    // ring.
    return uid;
  }

  /**
   * Calculate slot index with the slot sequence (sequence mod bufferSize).
   *
   * @param sequence sequence number
   * @return slot index
   */
  private int calSlotIndex(long sequence) {
    return (int) (sequence & indexMask);
  }

  /**
   * Initialize flags as {@value #CAN_PUT_FLAG}.
   *
   * @param bufferSize buffer size
   * @return flags array
   */
  private PaddedAtomicLong[] initFlags(int bufferSize) {
    PaddedAtomicLong[] flags = new PaddedAtomicLong[bufferSize];
    for (int i = 0; i < bufferSize; i++) {
      flags[i] = new PaddedAtomicLong(CAN_PUT_FLAG);
    }
    return flags;
  }

  /**
   * Discard policy for {@link RejectedPutBufferHandler}, just do logging.
   *
   * @param ringBuffer ring buffer
   * @param uid UID to put
   */
  private void discardPutBuffer(RingBuffer ringBuffer, long uid) {
    LOGGER.warn("rejected putting buffer for uid: {}. {}", uid, ringBuffer);
  }

  /**
   * Policy for {@link RejectedTakeBufferHandler}, throws {@link RuntimeException} after logging.
   *
   * @param ringBuffer ring buffer
   */
  private void exceptionRejectedTakeBuffer(RingBuffer ringBuffer) {
    LOGGER.warn("rejected take buffer. {}", ringBuffer);
    throw new RuntimeException("rejected take buffer. " + ringBuffer);
  }

  // Getters
  public long getTail() {
    return tail.get();
  }

  public long getCursor() {
    return cursor.get();
  }

  public int getBufferSize() {
    return bufferSize;
  }

  // Setters
  public void setRejectedPutHandler(RejectedPutBufferHandler rejectedPutHandler) {
    this.rejectedPutHandler = rejectedPutHandler;
  }

  public void setRejectedTakeHandler(RejectedTakeBufferHandler rejectedTakeHandler) {
    this.rejectedTakeHandler = rejectedTakeHandler;
  }

  public void setBufferPaddingExecutor(BufferPaddingExecutor bufferPaddingExecutor) {
    this.bufferPaddingExecutor = bufferPaddingExecutor;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("bufferSize", bufferSize)
        .add("tail", tail)
        .add("cursor", cursor)
        .add("paddingThreshold", paddingThreshold)
        .toString();
  }
}
