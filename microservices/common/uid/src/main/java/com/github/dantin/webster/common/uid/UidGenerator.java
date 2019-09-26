package com.github.dantin.webster.common.uid;

import com.github.dantin.webster.common.uid.exception.UidGenerateException;
import com.google.common.base.Strings;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/** An interface that generate Unique ID. */
public interface UidGenerator {

  String DAY_PATTERN = "yyyy-MM-dd";
  DateTimeFormatter EPOCH_FORMAT =
      DateTimeFormat.forPattern(DAY_PATTERN).withZone(DateTimeZone.UTC);

  static DefaultUidGenerator.DefaultUidGeneratorBuilder defaultUidGeneratorBuilder(long workerId) {
    return new DefaultUidGenerator.DefaultUidGeneratorBuilder(workerId);
  }

  static CachedUidGenerator.CachedUidGeneratorBuilder cachedUidGeneratorBuilder(long workerId) {
    return new CachedUidGenerator.CachedUidGeneratorBuilder(workerId);
  }

  /**
   * Get an unique ID.
   *
   * @return UID
   * @throws UidGenerateException exception
   */
  long getUID() throws UidGenerateException;

  /**
   * Parse the meta elements which are used to generate the UID.
   *
   * @param uid UID
   * @return parsed string
   */
  String parseUID(long uid);
}

class UidGeneratorBuilder {

  private final long workerId;

  private int timeBits = 28;
  private int workerBits = 22;
  private int seqBits = 13;

  /* Customized epoch, unit as second. E.g., 2016-05-20 (ms:1463673600000) */
  private String epoch = "2016-05-20";
  private long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1463673600000L);

  UidGeneratorBuilder(long workerId) {
    if (workerId < 0) {
      throw new RuntimeException("worker id " + workerId + " is invalid");
    }
    this.workerId = workerId;
  }

  // Getters
  long getWorkerId() {
    return workerId;
  }

  int getTimeBits() {
    return timeBits;
  }

  // Setters
  void setTimeBits(int timeBits) {
    if (timeBits > 0) {
      this.timeBits = timeBits;
    }
  }

  int getWorkerBits() {
    return workerBits;
  }

  void setWorkerBits(int workerBits) {
    if (workerBits > 0) {
      this.workerBits = workerBits;
    }
  }

  int getSeqBits() {
    return seqBits;
  }

  void setSeqBits(int seqBits) {
    if (seqBits > 0) {
      this.seqBits = seqBits;
    }
  }

  String getEpoch() {
    return epoch;
  }

  void setEpoch(String epoch) {
    if (!Strings.isNullOrEmpty(epoch)) {
      this.epoch = epoch;
      this.epochSeconds =
          TimeUnit.MILLISECONDS.toSeconds(
              UidGenerator.EPOCH_FORMAT.parseDateTime(epoch).getMillis());
    }
  }

  long getEpochSeconds() {
    return epochSeconds;
  }
}
