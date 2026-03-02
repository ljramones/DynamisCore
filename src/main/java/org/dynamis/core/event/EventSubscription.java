package org.dynamis.core.event;

import java.util.Objects;

/**
 * Subscription handle for event listener registration.
 *
 * @param subscriptionId unique subscription identifier
 * @param eventType subscribed event type
 * @param unsubscribe callback used to cancel the subscription
 */
public record EventSubscription(
    long subscriptionId, Class<? extends EngineEvent> eventType, Runnable unsubscribe) {
  /**
   * Creates a validated subscription instance.
   *
   * @param subscriptionId unique subscription identifier
   * @param eventType subscribed event type
   * @param unsubscribe callback used to cancel the subscription
   * @return subscription instance
   */
  public static EventSubscription of(
      long subscriptionId, Class<? extends EngineEvent> eventType, Runnable unsubscribe) {
    return new EventSubscription(subscriptionId, eventType, unsubscribe);
  }

  /**
   * Validates subscription values.
   *
   * @param subscriptionId unique subscription identifier
   * @param eventType subscribed event type
   * @param unsubscribe callback used to cancel the subscription
   */
  public EventSubscription {
    if (subscriptionId <= 0L) {
      throw new IllegalArgumentException(
          "subscriptionId must be positive, got: " + subscriptionId);
    }
    Objects.requireNonNull(eventType, "eventType must not be null");
    Objects.requireNonNull(unsubscribe, "unsubscribe must not be null");
  }

  /**
   * Cancels this subscription.
   */
  public void cancel() {
    unsubscribe.run();
  }
}
