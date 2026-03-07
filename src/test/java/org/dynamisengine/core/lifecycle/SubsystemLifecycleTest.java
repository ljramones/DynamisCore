package org.dynamisengine.core.lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.dynamisengine.core.config.DynamisConfig;
import org.junit.jupiter.api.Test;

class SubsystemLifecycleTest {
  @Test
  void tickContextRejectsNegativeTickNumber() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> TickContext.of(-1L, 0.1d, 1.0d));
    assertEquals("tickNumber must be >= 0, got: -1", ex.getMessage());
  }

  @Test
  void tickContextRejectsNegativeDeltaTime() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> TickContext.of(0L, -0.1d, 1.0d));
    assertEquals("deltaTime must be >= 0.0, got: -0.1", ex.getMessage());
  }

  @Test
  void tickContextRejectsNegativeElapsedTime() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> TickContext.of(0L, 0.1d, -1.0d));
    assertEquals("elapsedTime must be >= 0.0, got: -1.0", ex.getMessage());
  }

  @Test
  void isFirstTickReportsTickZeroOnly() {
    assertTrue(TickContext.of(0L, 0.1d, 0.1d).isFirstTick());
    assertFalse(TickContext.of(1L, 0.1d, 0.2d).isFirstTick());
  }

  @Test
  void tickContextSupportsEqualityAndHashing() {
    TickContext first = TickContext.of(2L, 0.016d, 1.25d);
    TickContext second = TickContext.of(2L, 0.016d, 1.25d);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void initContextFactoryCarriesConfig() {
    DynamisConfig config = DynamisConfig.builder().set("mode", "test").build();

    InitContext context = InitContext.of(config);

    assertEquals(config, context.config());
  }

  @Test
  void subsystemNameDefaultsToSimpleClassName() {
    DynamisSubsystem subsystem =
        new DynamisSubsystem() {
          @Override
          public void initialize(InitContext ctx) {}

          @Override
          public void tick(TickContext ctx) {}

          @Override
          public void shutdown() {}
        };

    assertEquals(subsystem.getClass().getSimpleName(), subsystem.subsystemName());
  }

  @Test
  void subsystemStateOrdinalOrderingMatchesLifecycleProgression() {
    assertTrue(SubsystemState.UNINITIALIZED.ordinal() < SubsystemState.RUNNING.ordinal());
    assertTrue(SubsystemState.RUNNING.ordinal() < SubsystemState.SHUTDOWN.ordinal());
  }
}
