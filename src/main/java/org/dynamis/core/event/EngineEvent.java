package org.dynamis.core.event;

/**
 * Marker contract for all engine events.
 *
 * <p>A sealed marker with an empty permits list is not valid Java syntax. This remains open so
 * other Dynamis components can define concrete event types.
 */
public interface EngineEvent {
  /**
   * Returns event dispatch priority.
   *
   * @return event priority
   */
  default EventPriority priority() {
    return EventPriority.NORMAL;
  }

  /**
   * Returns an event timestamp in nanoseconds.
   *
   * <p>Default behavior is call-time, not creation-time, unless overridden by event types.
   *
   * @return timestamp in nanoseconds
   */
  default long timestamp() {
    return System.nanoTime();
  }
}
