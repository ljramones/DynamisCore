package org.dynamis.core.event;

/**
 * Listener for a specific event type.
 *
 * @param <T> event type
 */
@FunctionalInterface
public interface EventListener<T extends EngineEvent> {
  /**
   * Handles an event.
   *
   * @param event event instance
   */
  void onEvent(T event);

  /**
   * Returns listener priority used for invocation ordering.
   *
   * @return listener priority
   */
  default EventPriority listenerPriority() {
    return EventPriority.NORMAL;
  }
}
