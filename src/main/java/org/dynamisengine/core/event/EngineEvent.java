package org.dynamisengine.core.event;

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
   * Returns the event timestamp in nanoseconds captured at event creation time.
   *
   * @return timestamp in nanoseconds
   */
  long timestamp();
}
