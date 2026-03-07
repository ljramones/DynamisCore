package org.dynamis.core.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EventBaseTypesTest {
  @Test
  void higherPriorityOrderingIsCorrect() {
    assertTrue(EventPriority.CRITICAL.isHigherThan(EventPriority.HIGH));
    assertTrue(EventPriority.HIGH.isHigherThan(EventPriority.NORMAL));
    assertTrue(EventPriority.NORMAL.isHigherThan(EventPriority.LOW));
  }

  @Test
  void enumNaturalOrderingMatchesPriority() {
    assertTrue(EventPriority.CRITICAL.compareTo(EventPriority.LOW) < 0);
    assertTrue(EventPriority.HIGH.compareTo(EventPriority.NORMAL) < 0);
  }

  @Test
  void engineEventDefaultsToNormalPriority() {
    EngineEvent event = new TestEvent();
    assertEquals(EventPriority.NORMAL, event.priority());
  }

  @Test
  void engineEventTimestampIsStableAndPositive() {
    EngineEvent event = new TestEvent();
    long first = event.timestamp();
    long second = event.timestamp();
    assertEquals(first, second);
    assertTrue(event.timestamp() > 0L);
  }

  @Test
  void eventListenerSupportsLambdaUsage() {
    final int[] counter = new int[] {0};
    EventListener<EngineEvent> listener = event -> counter[0]++;

    listener.onEvent(new TestEvent());

    assertEquals(1, counter[0]);
  }

  @Test
  void eventSubscriptionRejectsInvalidValues() {
    IllegalArgumentException zeroId =
        assertThrows(
            IllegalArgumentException.class,
            () -> EventSubscription.of(0L, EngineEvent.class, () -> {}));
    assertEquals("subscriptionId must be positive, got: 0", zeroId.getMessage());

    IllegalArgumentException negativeId =
        assertThrows(
            IllegalArgumentException.class,
            () -> EventSubscription.of(-1L, EngineEvent.class, () -> {}));
    assertEquals("subscriptionId must be positive, got: -1", negativeId.getMessage());

    NullPointerException nullType =
        assertThrows(NullPointerException.class, () -> EventSubscription.of(1L, null, () -> {}));
    assertEquals("eventType must not be null", nullType.getMessage());

    NullPointerException nullUnsubscribe =
        assertThrows(
            NullPointerException.class,
            () -> EventSubscription.of(1L, EngineEvent.class, null));
    assertEquals("unsubscribe must not be null", nullUnsubscribe.getMessage());
  }

  @Test
  void eventSubscriptionCancelInvokesUnsubscribe() {
    final int[] counter = new int[] {0};
    EventSubscription subscription =
        EventSubscription.of(1L, EngineEvent.class, () -> counter[0]++);

    subscription.cancel();

    assertEquals(1, counter[0]);
  }

  private static final class TestEvent implements EngineEvent {
    private final long timestamp = System.nanoTime();

    @Override
    public long timestamp() {
      return timestamp;
    }
  }
}
