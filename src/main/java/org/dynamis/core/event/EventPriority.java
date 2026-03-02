package org.dynamis.core.event;

/**
 * Dispatch priority for events and listeners.
 */
public enum EventPriority {
  CRITICAL(0),
  HIGH(1),
  NORMAL(2),
  LOW(3);

  private final int level;

  EventPriority(int level) {
    this.level = level;
  }

  /**
   * Returns whether this priority is higher than the other priority.
   *
   * @param other priority to compare against
   * @return true when this priority ranks before other
   */
  public boolean isHigherThan(EventPriority other) {
    return this.level < other.level;
  }
}
