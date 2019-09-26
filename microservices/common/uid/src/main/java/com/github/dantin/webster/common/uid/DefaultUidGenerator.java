package com.github.dantin.webster.common.uid;

import com.github.dantin.webster.common.base.Builder;
import com.github.dantin.webster.common.uid.exception.UidGenerateException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link UidGenerator}
 *
 * <p>The unique id has 64bits (long), default allocated as blow:<br>
 * <li>sign: The highest bit is 0
 * <li>delta seconds: The next 28 bits, represents delta seconds since a customer epoch(2016-05-20
 *     00:00:00.000). Supports about 8.7 years until to 2024-11-20 21:24:16
 * <li>worker id: The next 22 bits, represents the worker's id which assigns based on database, max
 *     id is about 420W
 * <li>sequence: The next 13 bits, represents a sequence within the same second, max for 8192/s<br>
 *     <br>
 *
 *     <p>The {@link DefaultUidGenerator#parseUID(long)} is a tool method to parse the bits
 *
 *     <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          28bits              22bits         13bits
 * }</pre>
 *
 *     <b>Note that:</b> The total bits must be 64 -1
 */
public class DefaultUidGenerator implements UidGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUidGenerator.class);

  private static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

  private static DateTimeFormatter EPOCH_DT_FORMAT =
      DateTimeFormat.forPattern(DATETIME_PATTERN).withZone(DateTimeZone.UTC);

  /** Bits allocate */
  protected int timeBits;

  protected int workerBits;
  protected int seqBits;

  /** Customized epoch, unit as second */
  protected String epoch;

  protected long epochSeconds;

  /** Stable fields after initializing */
  protected BitsAllocator bitsAllocator;

  protected long workerId;

  /** Volatile fields cause by nextId() */
  protected long sequence = 0L;

  protected long lastSecond = -1L;

  /**
   * Constructor using parameters.
   *
   * @param builder builder
   */
  DefaultUidGenerator(UidGeneratorBuilder builder) {
    this.workerId = builder.getWorkerId();
    this.timeBits = builder.getTimeBits();
    this.workerBits = builder.getWorkerBits();
    this.seqBits = builder.getSeqBits();
    this.epoch = builder.getEpoch();
    this.epochSeconds = builder.getEpochSeconds();

    // initialize bits allocator.
    bitsAllocator = new BitsAllocator(this.timeBits, this.workerBits, this.seqBits);

    // initialize worker id.
    if (workerId > bitsAllocator.getMaxWorkerId()) {
      throw new RuntimeException(
          "worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
    }

    LOGGER.info(
        "initialize bits(1, {}, {}, {}) for workerID: {}", timeBits, workerBits, seqBits, workerId);
  }

  @Override
  public long getUID() throws UidGenerateException {
    try {
      return nextId();
    } catch (Exception e) {
      LOGGER.error("generate unique id error. ", e);
      throw new UidGenerateException(e);
    }
  }

  @Override
  public String parseUID(long uid) {
    long totalBits = BitsAllocator.TOTAL_BITS;
    long signBits = bitsAllocator.getSignBits();
    long timestampBits = bitsAllocator.getTimestampBits();
    long workerIdBits = bitsAllocator.getWorkerIdBits();
    long sequenceBits = bitsAllocator.getSequenceBits();

    // parse UID
    long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
    long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
    long deltaSeconds = uid >>> (workerIdBits + sequenceBits);

    Date timestamp = new Date(TimeUnit.SECONDS.toMillis(epochSeconds + deltaSeconds));
    return String.format(
        "{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"",
        uid, EPOCH_DT_FORMAT.print(timestamp.getTime()), workerId, sequence);
  }

  protected synchronized long nextId() {
    long currentSecond = getCurrentSecond();

    if (currentSecond < lastSecond) {
      long refusedSeconds = lastSecond - currentSecond;
      throw new UidGenerateException(
          "clock moved backwards, refusing for %d seconds", refusedSeconds);
    }

    if (currentSecond == lastSecond) {
      // At the same second, increase sequence.
      sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
      // Exceed the max sequence, we wait the next second to generate uid.
      if (sequence == 0) {
        currentSecond = getNextSecond(lastSecond);
      }
    } else {
      // At the different second, sequence restart from zero.
      sequence = 0L;
    }

    lastSecond = currentSecond;

    // Allocate bits for UID.
    return bitsAllocator.allocate(currentSecond - epochSeconds, workerId, sequence);
  }

  private long getCurrentSecond() {
    long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    if (currentSecond - epochSeconds > bitsAllocator.getMaxDeltaSeconds()) {
      throw new UidGenerateException(
          "timestamp bits is exhausted, refusing UID generate. Now: ", currentSecond);
    }
    return currentSecond;
  }

  private long getNextSecond(long lastTimestamp) {
    long timestamp = getCurrentSecond();
    while (timestamp <= lastTimestamp) {
      timestamp = getCurrentSecond();
    }
    return timestamp;
  }

  public static class DefaultUidGeneratorBuilder implements Builder<DefaultUidGenerator> {

    private final UidGeneratorBuilder uidGeneratorBuilder;

    DefaultUidGeneratorBuilder(long workerId) {
      this.uidGeneratorBuilder = new UidGeneratorBuilder(workerId);
    }

    public DefaultUidGeneratorBuilder timeBits(int timeBits) {
      uidGeneratorBuilder.setTimeBits(timeBits);
      return this;
    }

    public DefaultUidGeneratorBuilder workerBits(int workerBits) {
      uidGeneratorBuilder.setWorkerBits(workerBits);
      return this;
    }

    public DefaultUidGeneratorBuilder seqBits(int seqBits) {
      uidGeneratorBuilder.setSeqBits(seqBits);
      return this;
    }

    public DefaultUidGeneratorBuilder epoch(String epoch) {
      uidGeneratorBuilder.setEpoch(epoch);
      return this;
    }

    @Override
    public DefaultUidGenerator build() {
      return new DefaultUidGenerator(this.uidGeneratorBuilder);
    }
  }
}
