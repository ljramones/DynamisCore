package org.dynamisengine.core.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe facade over JUL for Dynamis components.
 */
public final class DynamisLogger {
  private final Logger logger;

  private DynamisLogger(Logger logger) {
    this.logger = logger;
  }

  /**
   * Gets a logger for the given class.
   *
   * @param clazz class used to derive logger name
   * @return logger facade
   */
  public static DynamisLogger get(Class<?> clazz) {
    if (clazz == null) {
      throw new IllegalArgumentException("Logger class must not be null");
    }
    return get(clazz.getName());
  }

  /**
   * Gets a logger by explicit name.
   *
   * @param name logger name
   * @return logger facade
   */
  public static DynamisLogger get(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Logger name must be non-null and non-empty");
    }
    return new DynamisLogger(Logger.getLogger(name));
  }

  /**
   * Returns the logger name.
   *
   * @return logger name
   */
  public String name() {
    return logger.getName();
  }

  /**
   * Returns whether logging is enabled for the provided level.
   *
   * @param level log level
   * @return true when enabled
   */
  public boolean isEnabled(LogLevel level) {
    return logger.isLoggable(toJulLevel(level));
  }

  /**
   * Sets logger threshold level.
   *
   * @param level minimum level to log
   */
  public void setLevel(LogLevel level) {
    logger.setLevel(toJulLevel(level));
  }

  /**
   * Logs a trace message.
   *
   * @param message log message
   */
  public void trace(String message) {
    logger.log(Level.FINEST, message);
  }

  /**
   * Logs a trace message with cause.
   *
   * @param message log message
   * @param cause throwable cause
   */
  public void trace(String message, Throwable cause) {
    logger.log(Level.FINEST, message, cause);
  }

  /**
   * Logs a debug message.
   *
   * @param message log message
   */
  public void debug(String message) {
    logger.log(Level.FINE, message);
  }

  /**
   * Logs a debug message with cause.
   *
   * @param message log message
   * @param cause throwable cause
   */
  public void debug(String message, Throwable cause) {
    logger.log(Level.FINE, message, cause);
  }

  /**
   * Logs an info message.
   *
   * @param message log message
   */
  public void info(String message) {
    logger.log(Level.INFO, message);
  }

  /**
   * Logs an info message with cause.
   *
   * @param message log message
   * @param cause throwable cause
   */
  public void info(String message, Throwable cause) {
    logger.log(Level.INFO, message, cause);
  }

  /**
   * Logs a warning message.
   *
   * @param message log message
   */
  public void warn(String message) {
    logger.log(Level.WARNING, message);
  }

  /**
   * Logs a warning message with cause.
   *
   * @param message log message
   * @param cause throwable cause
   */
  public void warn(String message, Throwable cause) {
    logger.log(Level.WARNING, message, cause);
  }

  /**
   * Logs an error message.
   *
   * @param message log message
   */
  public void error(String message) {
    logger.log(Level.SEVERE, message);
  }

  /**
   * Logs an error message with cause.
   *
   * @param message log message
   * @param cause throwable cause
   */
  public void error(String message, Throwable cause) {
    logger.log(Level.SEVERE, message, cause);
  }

  private static Level toJulLevel(LogLevel level) {
    if (level == null) {
      throw new IllegalArgumentException("Log level must not be null");
    }
    return switch (level) {
      case TRACE -> Level.FINEST;
      case DEBUG -> Level.FINE;
      case INFO -> Level.INFO;
      case WARN -> Level.WARNING;
      case ERROR -> Level.SEVERE;
    };
  }
}
