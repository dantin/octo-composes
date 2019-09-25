package com.github.dantin.webster.common.base;

import com.google.common.base.Strings;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Named thread in ThreadFactory. If there is no specified name for thread, it will auto detect
 * using the invoker's class name instead.
 */
public class NamingThreadFactory implements ThreadFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(NamingThreadFactory.class);
  /* Sequence for multi-thread name prefix */
  private final ConcurrentHashMap<String, AtomicLong> sequences;
  /* Thread name prefix */
  private String name;
  /* Is daemon thread */
  private boolean daemon;
  /* UncaughtExceptionHandler */
  private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

  /** Default constructor. */
  public NamingThreadFactory() {
    this(null, false, null);
  }
  /**
   * Constructor using parameters.
   *
   * @param name name prefix
   */
  public NamingThreadFactory(String name) {
    this(name, false, null);
  }

  /**
   * Constructor using parameters.
   *
   * @param name name prefix
   * @param daemon is daemon thread
   */
  public NamingThreadFactory(String name, boolean daemon) {
    this(name, daemon, null);
  }

  /**
   * Constructor using parameters.
   *
   * @param name name prefix
   * @param daemon is daemon thread
   * @param uncaughtExceptionHandler exception handler
   */
  public NamingThreadFactory(
      String name, boolean daemon, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
    this.name = name;
    this.daemon = daemon;
    this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    this.sequences = new ConcurrentHashMap<>();
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(r);
    thread.setDaemon(this.daemon);

    String prefix = this.name;
    if (Strings.isNullOrEmpty(prefix)) {
      prefix = getInvoker(2);
    }
    thread.setName(prefix + "-" + getSequence(prefix));

    if (this.uncaughtExceptionHandler != null) {
      thread.setUncaughtExceptionHandler(this.uncaughtExceptionHandler);
    } else {
      thread.setUncaughtExceptionHandler(
          (t, e) ->
              LOGGER.error("unhandled exception in thread: " + t.getId() + ":" + t.getName(), e));
    }

    return thread;
  }

  private String getInvoker(int depth) {
    Exception e = new Exception();
    StackTraceElement[] stackTraceElements = e.getStackTrace();
    if (stackTraceElements.length > depth) {
      return ClassUtils.getShortClassName(stackTraceElements[depth].getClassName());
    }
    return getClass().getSimpleName();
  }

  private long getSequence(String invoker) {
    AtomicLong r = this.sequences.get(invoker);
    if (r == null) {
      r = new AtomicLong(0);
      AtomicLong previous = this.sequences.putIfAbsent(invoker, r);
      if (previous != null) {
        r = previous;
      }
    }
    return r.incrementAndGet();
  }

  // Getters
  public String getName() {
    return name;
  }

  // Setters
  public void setName(String name) {
    this.name = name;
  }

  public boolean isDaemon() {
    return daemon;
  }

  public void setDaemon(boolean daemon) {
    this.daemon = daemon;
  }

  public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
    return uncaughtExceptionHandler;
  }

  public void setUncaughtExceptionHandler(
      Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
    this.uncaughtExceptionHandler = uncaughtExceptionHandler;
  }
}
