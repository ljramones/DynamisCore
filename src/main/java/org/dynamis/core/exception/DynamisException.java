package org.dynamis.core.exception;

/**
 * Root unchecked exception for Dynamis components.
 */
public class DynamisException extends RuntimeException {
  /**
   * Creates an exception with a message.
   *
   * @param message exception message
   */
  public DynamisException(String message) {
    super(message);
  }

  /**
   * Creates an exception with a message and cause.
   *
   * @param message exception message
   * @param cause root cause
   */
  public DynamisException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates an exception with a cause.
   *
   * @param cause root cause
   */
  public DynamisException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates an exception with full control over suppression and stack trace behavior.
   *
   * @param message exception message
   * @param cause root cause
   * @param enableSuppression whether suppression is enabled
   * @param writableStackTrace whether stack trace is writable
   */
  public DynamisException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * Returns the component associated with this exception.
   *
   * @return component name
   */
  public String component() {
    return "dynamis-core";
  }

  /**
   * Returns whether this exception is considered recoverable.
   *
   * @return true when recoverable
   */
  public boolean isRecoverable() {
    return false;
  }
}
