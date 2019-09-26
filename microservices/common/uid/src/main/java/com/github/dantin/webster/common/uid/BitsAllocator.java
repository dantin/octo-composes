package com.github.dantin.webster.common.uid;

import com.google.common.base.MoreObjects;

/** BitsAllocator allocate 64 bits */
public class BitsAllocator {

  /* Total 64 bits */
  public static final int TOTAL_BITS = 1 << 6;
  private final int timestampBits;
  private final int workerIdBits;
  private final int sequenceBits;
  /* Max value for workerId & sequence */
  private final long maxDeltaSeconds;
  private final long maxWorkerId;
  private final long maxSequence;
  /* Shift for timestamp & workerId */
  private final int timestampShift;
  private final int workerIdShift;
  /* Bits: [sign | second | workerId | sequence] */
  private int signBits = 1;

  /**
   * Constructor with parameters.
   *
   * @param timestampBits timestamp bits
   * @param workerIdBits worker ID bits
   * @param sequenceBits sequence bits
   */
  public BitsAllocator(int timestampBits, int workerIdBits, int sequenceBits) {
    int allocateTotalBits = signBits + timestampBits + workerIdBits + sequenceBits;
    if (allocateTotalBits != TOTAL_BITS) {
      throw new RuntimeException("allocate not enough 64 bits");
    }

    // initialize bits
    this.timestampBits = timestampBits;
    this.workerIdBits = workerIdBits;
    this.sequenceBits = sequenceBits;

    // initialize max value
    this.maxDeltaSeconds = ~(-1L << timestampBits);
    this.maxWorkerId = ~(-1L << workerIdBits);
    this.maxSequence = ~(-1L << sequenceBits);

    // initialize shift
    this.timestampShift = workerIdBits + sequenceBits;
    this.workerIdShift = sequenceBits;
  }

  /**
   * Allocate bits for UID. <b>Note:</b> The highest bit will always be 0 for sign
   *
   * @param deltaSeconds time delta in seconds
   * @param workerId worker ID
   * @param sequence sequence
   * @return UID
   */
  public long allocate(long deltaSeconds, long workerId, long sequence) {
    return (deltaSeconds << this.timestampShift | (workerId << this.workerIdShift)) | sequence;
  }

  // Getters
  public int getSignBits() {
    return signBits;
  }

  public int getTimestampBits() {
    return timestampBits;
  }

  public int getWorkerIdBits() {
    return workerIdBits;
  }

  public int getSequenceBits() {
    return sequenceBits;
  }

  public long getMaxDeltaSeconds() {
    return maxDeltaSeconds;
  }

  public long getMaxWorkerId() {
    return maxWorkerId;
  }

  public long getMaxSequence() {
    return maxSequence;
  }

  public int getTimestampShift() {
    return timestampShift;
  }

  public int getWorkerIdShift() {
    return workerIdShift;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("signBits", signBits)
        .add("timestampBits", timestampBits)
        .add("workerIdBits", workerIdBits)
        .add("sequenceBits", sequenceBits)
        .add("maxDeltaSeconds", maxDeltaSeconds)
        .add("maxWorkerId", maxWorkerId)
        .add("maxSequence", maxSequence)
        .add("timestampShift", timestampShift)
        .add("workerIdShift", workerIdShift)
        .toString();
  }
}
