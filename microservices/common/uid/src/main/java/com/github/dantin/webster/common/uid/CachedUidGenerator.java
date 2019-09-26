package com.github.dantin.webster.common.uid;

import com.github.dantin.webster.common.base.Builder;
import com.github.dantin.webster.common.uid.buffer.BufferPaddingExecutor;
import com.github.dantin.webster.common.uid.buffer.RejectedPutBufferHandler;
import com.github.dantin.webster.common.uid.buffer.RejectedTakeBufferHandler;
import com.github.dantin.webster.common.uid.buffer.RingBuffer;
import com.github.dantin.webster.common.uid.exception.UidGenerateException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a cached implementation of {@link UidGenerator} extends from {@link
 * DefaultUidGenerator}, based on a lock free {@link RingBuffer}.
 *
 * <p>Some settings can be specified as below:
 * <li><b>boostPower:</b> RingBuffer size boost for a power of 2, Sample: boostPower is 3, it means
 *     the buffer size will be <code>({@link BitsAllocator#getMaxSequence()} + 1) &lt;&lt;
 * {@link #boostPower}</code>, Default as {@link CachedUidGeneratorBuilder#DEFAULT_BOOST_POWER}
 * <li><b>paddingFactor:</b> Represents a percent value of (0 - 100). When the count of rest
 *     available UIDs reach the threshold, it will trigger padding buffer. Default as{@link
 *     RingBuffer#DEFAULT_PADDING_PERCENT} Sample: paddingFactor=20, bufferSize=1000 ->
 *     threshold=1000 * 20 /100, padding buffer will be triggered when tail-cursor<threshold
 * <li><b>scheduleInterval:</b> Padding buffer in a schedule, specify padding buffer interval, Unit
 *     as second
 * <li><b>rejectedPutBufferHandler:</b> Policy for rejected put buffer. Default as discard put
 *     request, just do logging
 * <li><b>rejectedTakeBufferHandler:</b> Policy for rejected take buffer. Default as throwing up an
 *     exception
 */
public class CachedUidGenerator extends DefaultUidGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(CachedUidGenerator.class);

  private int boostPower;
  private int paddingFactor;

  private Long scheduleInterval;
  private RejectedPutBufferHandler rejectedPutBufferHandler;
  private RejectedTakeBufferHandler rejectedTakeBufferHandler;

  /* RingBuffer */
  private RingBuffer ringBuffer;
  private BufferPaddingExecutor bufferPaddingExecutor;

  /**
   * Constructor using parameter.
   *
   * @param builder builder
   */
  private CachedUidGenerator(CachedUidGeneratorBuilder builder) {
    // initialize workerId and bitsAllocator
    super(builder.uidGeneratorBuilder);

    // initialize CachedUidGenerator specific settings
    this.boostPower = builder.boostPower;
    this.paddingFactor = builder.paddingFactor;
    this.scheduleInterval = builder.scheduleInterval;
    this.rejectedPutBufferHandler = builder.rejectedPutBufferHandler;
    this.rejectedTakeBufferHandler = builder.rejectedTakeBufferHandler;

    // initialize RingBuffer & RingBufferPaddingExecutor
    this.initRingBuffer();
    LOGGER.info("initialize RingBuffer successfully.");
  }

  @Override
  public long getUID() {
    try {
      return ringBuffer.take();
    } catch (Exception e) {
      LOGGER.error("generate unique id exception. ", e);
      throw new UidGenerateException(e);
    }
  }

  @Override
  public String parseUID(long uid) {
    return super.parseUID(uid);
  }

  /** Destroy threads pool on destroy. */
  public void destroy() {
    bufferPaddingExecutor.shutdown();
  }

  /**
   * Get the UIDs in the same specified second under the max sequence.
   *
   * @param currentSecond current timestamp in second
   * @return UID list, size of {@link BitsAllocator#getMaxSequence()} + 1
   */
  protected List<Long> nextIdsForOneSecond(long currentSecond) {
    // initialize result list size of (max sequence + 1)
    int listSize = (int) bitsAllocator.getMaxSequence() + 1;
    List<Long> uidList = new ArrayList<>(listSize);

    // allocate the first sequence of the second, the others can be calculated with the offset
    long firstSeqUid = bitsAllocator.allocate(currentSecond - epochSeconds, workerId, 0L);
    for (int offset = 0; offset < listSize; offset++) {
      uidList.add(firstSeqUid + offset);
    }
    return uidList;
  }

  /** Initialize RingBuffer and RingBufferPaddingExecutor. */
  private void initRingBuffer() {
    // initialize RingBuffer
    int bufferSize = ((int) bitsAllocator.getMaxSequence() + 1) << boostPower;
    this.ringBuffer = new RingBuffer(bufferSize, paddingFactor);
    LOGGER.info("initialize ring buffer size: {}, paddingFactor: {}", bufferSize, paddingFactor);

    // initialize RingBufferPaddingExecutor
    boolean usingSchedule = (scheduleInterval != null);
    this.bufferPaddingExecutor =
        new BufferPaddingExecutor(ringBuffer, this::nextIdsForOneSecond, usingSchedule);
    if (usingSchedule) {
      bufferPaddingExecutor.setScheduleInterval(scheduleInterval);
    }
    LOGGER.info(
        "initialize BufferPaddingExecutor, using schedule: {}, interval: {}",
        usingSchedule,
        scheduleInterval);

    // set rejected put/take handle policy
    this.ringBuffer.setBufferPaddingExecutor(bufferPaddingExecutor);
    if (rejectedPutBufferHandler != null) {
      this.ringBuffer.setRejectedPutHandler(rejectedPutBufferHandler);
    }
    if (rejectedTakeBufferHandler != null) {
      this.ringBuffer.setRejectedTakeHandler(rejectedTakeBufferHandler);
    }

    // fill in all slots of the RingBuffer
    bufferPaddingExecutor.paddingBuffer();

    // start buffer padding threads
    bufferPaddingExecutor.start();
  }

  public static class CachedUidGeneratorBuilder implements Builder<CachedUidGenerator> {

    public static final int DEFAULT_BOOST_POWER = 3;

    private final UidGeneratorBuilder uidGeneratorBuilder;

    private int boostPower = DEFAULT_BOOST_POWER;
    private int paddingFactor = RingBuffer.DEFAULT_PADDING_PERCENT;
    private Long scheduleInterval;
    private RejectedPutBufferHandler rejectedPutBufferHandler;
    private RejectedTakeBufferHandler rejectedTakeBufferHandler;

    CachedUidGeneratorBuilder(long workerId) {
      this.uidGeneratorBuilder = new UidGeneratorBuilder(workerId);
    }

    public CachedUidGeneratorBuilder timeBits(int timeBits) {
      uidGeneratorBuilder.setTimeBits(timeBits);
      return this;
    }

    public CachedUidGeneratorBuilder workerBits(int workerBits) {
      uidGeneratorBuilder.setWorkerBits(workerBits);
      return this;
    }

    public CachedUidGeneratorBuilder seqBits(int seqBits) {
      uidGeneratorBuilder.setSeqBits(seqBits);
      return this;
    }

    public CachedUidGeneratorBuilder epoch(String epoch) {
      uidGeneratorBuilder.setEpoch(epoch);
      return this;
    }

    public CachedUidGeneratorBuilder boostPower(int boostPower) {
      if (boostPower > 0) {
        this.boostPower = boostPower;
      }
      return this;
    }

    public CachedUidGeneratorBuilder paddingFactor(int paddingFactor) {
      if (paddingFactor > 0 && paddingFactor < 100) {
        this.paddingFactor = paddingFactor;
      }
      return this;
    }

    public CachedUidGeneratorBuilder scheduleInterval(Long scheduleInterval) {
      if (scheduleInterval != null && scheduleInterval > 0) {
        this.scheduleInterval = scheduleInterval;
      }
      return this;
    }

    public CachedUidGeneratorBuilder rejectedPutBufferHandler(
        RejectedPutBufferHandler rejectedPutBufferHandler) {
      if (rejectedPutBufferHandler != null) {
        this.rejectedPutBufferHandler = rejectedPutBufferHandler;
      }
      return this;
    }

    public CachedUidGeneratorBuilder rejectedTakeBufferHandler(
        RejectedTakeBufferHandler rejectedTakeBufferHandler) {
      if (rejectedTakeBufferHandler != null) {
        this.rejectedTakeBufferHandler = rejectedTakeBufferHandler;
      }
      return this;
    }

    @Override
    public CachedUidGenerator build() {
      return new CachedUidGenerator(this);
    }
  }
}
