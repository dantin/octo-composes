package com.github.dantin.webster.common.uid.buffer;

import com.github.dantin.webster.common.base.NamingThreadFactory;
import com.github.dantin.webster.common.uid.utils.PaddedAtomicLong;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executors for padding {@link RingBuffer}.
 *
 * <p>There are two kinds of executors:
 * <li>the one for scheduled padding
 * <li>the other for immediate padding
 */
public class BufferPaddingExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(BufferPaddingExecutor.class);

  /* Constants */
  private static final String WORKER_NAME = "RingBuffer-Padding-Worker";
  private static final String SCHEDULE_NAME = "RingBuffer-Padding-Schedule";
  private static final long DEFAULT_SCHEDULE_INTERVAL = 5 * 60L; // 5 minutes

  /* Whether buffer padding is running */
  private final AtomicBoolean running;

  /* We can borrow UIDs from the future, here store the last second we have consumed */
  private final PaddedAtomicLong lastSecond;

  /* RingBuffer & BufferedUidProvider */
  private final RingBuffer ringBuffer;
  private final BufferedUidProvider uidProvider;

  /* Padding immediately by the thread pool */
  private final ExecutorService bufferPadExecutors;
  /* Padding schedule thread */
  private final ScheduledExecutorService bufferPadSchedule;

  /* Schedule interval Unit as seconds */
  private long scheduleInterval = DEFAULT_SCHEDULE_INTERVAL;

  /**
   * Constructor with parameters, default use schedule.
   *
   * @param ringBuffer ring buffer
   * @param uidProvider UID provider
   */
  public BufferPaddingExecutor(RingBuffer ringBuffer, BufferedUidProvider uidProvider) {
    this(ringBuffer, uidProvider, true);
  }

  /**
   * Constructor with parameters.
   *
   * @param ringBuffer ring buffer
   * @param uidProvider UID provider
   * @param usingSchedule whether use schedule padding
   */
  public BufferPaddingExecutor(
      RingBuffer ringBuffer, BufferedUidProvider uidProvider, boolean usingSchedule) {
    this.running = new AtomicBoolean(false);
    this.lastSecond =
        new PaddedAtomicLong(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
    this.ringBuffer = ringBuffer;
    this.uidProvider = uidProvider;

    // initialize thread pool
    int cores = Runtime.getRuntime().availableProcessors();
    bufferPadExecutors =
        Executors.newFixedThreadPool(cores * 2, new NamingThreadFactory(WORKER_NAME));

    // initialize schedule thread
    if (usingSchedule) {
      bufferPadSchedule =
          Executors.newSingleThreadScheduledExecutor(new NamingThreadFactory(SCHEDULE_NAME));
    } else {
      bufferPadSchedule = null;
    }
  }

  /** Start executors. */
  public void start() {
    if (bufferPadSchedule != null) {
      bufferPadSchedule.scheduleAtFixedRate(
          this::paddingBuffer, scheduleInterval, scheduleInterval, TimeUnit.SECONDS);
    }
  }

  /** Shutdown executors. */
  public void shutdown() {
    if (!bufferPadExecutors.isShutdown()) {
      bufferPadExecutors.shutdown();
    }

    if (bufferPadSchedule != null && !bufferPadSchedule.isShutdown()) {
      bufferPadSchedule.shutdown();
    }
  }

  /**
   * Check whether is padding.
   *
   * @return checking result
   */
  public boolean isRunning() {
    return running.get();
  }

  /** Padding buffer in the thread pool. */
  void asyncPadding() {
    bufferPadExecutors.submit(this::paddingBuffer);
  }

  /** Padding buffer to fill the slots until to catch the cursor. */
  public void paddingBuffer() {
    LOGGER.info("ready to padding buffer lastSecond: {}. {}", lastSecond.get(), ringBuffer);

    // is still running
    if (!running.compareAndSet(false, true)) {
      LOGGER.info("padding buffer is still running. {}", ringBuffer);
      return;
    }

    // fill the rest slots untill to catch the cursor
    boolean isFullRingBuffer = false;
    while (!isFullRingBuffer) {
      List<Long> uidList = uidProvider.provide(lastSecond.incrementAndGet());
      for (Long uid : uidList) {
        isFullRingBuffer = !ringBuffer.put(uid);
        if (isFullRingBuffer) {
          break;
        }
      }
    }

    // not running now
    running.compareAndSet(true, false);
    LOGGER.info("end to padding buffer lastSecond: {}. {}", lastSecond.get(), ringBuffer);
  }

  public void setScheduleInterval(long scheduleInterval) {
    if (scheduleInterval <= 0) {
      throw new RuntimeException("schedule interval must be positive");
    }
    this.scheduleInterval = scheduleInterval;
  }
}
