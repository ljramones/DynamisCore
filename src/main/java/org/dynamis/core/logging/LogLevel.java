package org.dynamis.core.logging;

/**
 * Logging verbosity levels ordered from most to least verbose.
 */
public enum LogLevel {
  TRACE(0),
  DEBUG(1),
  INFO(2),
  WARN(3),
  ERROR(4);

  private final int level;

  LogLevel(int level) {
    this.level = level;
  }

  /**
   * Returns whether this level is enabled when the given threshold is active.
   *
   * @param threshold configured minimum level
   * @return true when this level should be logged
   */
  public boolean isEnabledAt(LogLevel threshold) {
    return this.level >= threshold.level;
  }
}
