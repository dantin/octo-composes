package com.github.dantin.webster.common.uid.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A padded {@link AtomicLong} to prevent the FalseSharing problem.
 *
 * <p>The CPU cache line commonly be 64 bytes, here is a sample of cache line after padding:
 *
 * <pre>{@code
 * 64 bytes = 8 bytes (object reference) + 6 * 8 bytes (padded long) + 8 bytes (a long value)
 * }</pre>
 */
public class PaddedAtomicLong extends AtomicLong {

  /* Padded 6 long (48 bytes) */
  public volatile long p1, p2, p3, p4, p5, p6 = 7L;

  /** Default constructor. */
  public PaddedAtomicLong() {
    super();
  }

  /**
   * Constructor using parameters.
   *
   * @param initialValue initial value
   */
  public PaddedAtomicLong(long initialValue) {
    super(initialValue);
  }

  /** To prevent GC optimizations for cleaning unused padded references. */
  public long sumPaddingToPreventOptimization() {
    return p1 + p2 + p3 + p4 + p5 + p6;
  }
}
