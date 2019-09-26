package com.github.dantin.webster.common.uid.exception;

public class UidGenerateException extends RuntimeException {

  /** Default constructor. */
  public UidGenerateException() {
    super();
  }

  /**
   * Constructor with parameters.
   *
   * @param message message
   */
  public UidGenerateException(String message) {
    super(message);
  }

  /**
   * Constructor with parameters.
   *
   * @param message message
   * @param cause cause
   */
  public UidGenerateException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor with message format.
   *
   * @param format message format
   * @param args arguments list
   */
  public UidGenerateException(String format, Object... args) {
    super(String.format(format, args));
  }

  /**
   * Constructor with cause.
   *
   * @param cause exception cause
   */
  public UidGenerateException(Throwable cause) {
    super(cause);
  }
}
