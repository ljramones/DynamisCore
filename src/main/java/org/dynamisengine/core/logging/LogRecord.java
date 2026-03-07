package org.dynamisengine.core.logging;

import java.util.Objects;

/**
 * Structured log entry for diagnostics and capture.
 *
 * @param level log level
 * @param loggerName logger name
 * @param message log message
 * @param cause optional throwable cause
 * @param timestampNanos timestamp from {@link System#nanoTime()}
 */
public record LogRecord(
    LogLevel level,
    String loggerName,
    String message,
    Throwable cause,
    long timestampNanos) {

  /**
   * Creates a log record without a cause.
   *
   * @param level log level
   * @param loggerName logger name
   * @param message log message
   * @return log record instance
   */
  public static LogRecord of(LogLevel level, String loggerName, String message) {
    return of(level, loggerName, message, null);
  }

  /**
   * Creates a log record with an optional cause.
   *
   * @param level log level
   * @param loggerName logger name
   * @param message log message
   * @param cause optional throwable cause
   * @return log record instance
   */
  public static LogRecord of(
      LogLevel level, String loggerName, String message, Throwable cause) {
    return new LogRecord(level, loggerName, message, cause, System.nanoTime());
  }

  /**
   * Validates log record fields.
   *
   * @param level log level
   * @param loggerName logger name
   * @param message log message
   * @param cause optional throwable cause
   * @param timestampNanos timestamp from {@link System#nanoTime()}
   */
  public LogRecord {
    Objects.requireNonNull(level, "level must not be null");
    Objects.requireNonNull(loggerName, "loggerName must not be null");
    Objects.requireNonNull(message, "message must not be null");
    cause = copyThrowable(cause);
  }

  @Override
  public Throwable cause() {
    return copyThrowable(this.cause);
  }

  private static Throwable copyThrowable(Throwable source) {
    if (source == null) {
      return null;
    }
    Throwable copy = new Throwable(source.getMessage());
    copy.setStackTrace(source.getStackTrace());
    return copy;
  }
}
