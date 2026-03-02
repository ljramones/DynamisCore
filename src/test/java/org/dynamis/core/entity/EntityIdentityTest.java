package org.dynamis.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EntityIdentityTest {
  @Test
  void recordsWithSameValueAreEqual() {
    assertEquals(EntityId.of(42L), EntityId.of(42L));
    assertEquals(ComponentId.of(7L), ComponentId.of(7L));
    assertEquals(SystemId.of(9L), SystemId.of(9L));
  }

  @Test
  void equalRecordsHaveEqualHashCodes() {
    assertEquals(EntityId.of(42L).hashCode(), EntityId.of(42L).hashCode());
    assertEquals(ComponentId.of(7L).hashCode(), ComponentId.of(7L).hashCode());
    assertEquals(SystemId.of(9L).hashCode(), SystemId.of(9L).hashCode());
  }

  @Test
  void compareToOrdersByIdAscending() {
    assertEquals(-1, EntityId.of(1L).compareTo(EntityId.of(2L)));
    assertEquals(-1, ComponentId.of(10L).compareTo(ComponentId.of(11L)));
    assertEquals(-1, SystemId.of(100L).compareTo(SystemId.of(101L)));
  }

  @Test
  void compareToThrowsOnNull() {
    assertThrows(NullPointerException.class, () -> EntityId.of(1L).compareTo(null));
    assertThrows(NullPointerException.class, () -> ComponentId.of(1L).compareTo(null));
    assertThrows(NullPointerException.class, () -> SystemId.of(1L).compareTo(null));
  }

  @Test
  void valueFactoriesRejectZeroAndNegativeValues() {
    IllegalArgumentException entityZero =
        assertThrows(IllegalArgumentException.class, () -> EntityId.of(0L));
    assertEquals("EntityId must be positive, got: 0", entityZero.getMessage());

    IllegalArgumentException entityNegative =
        assertThrows(IllegalArgumentException.class, () -> EntityId.of(-1L));
    assertEquals("EntityId must be positive, got: -1", entityNegative.getMessage());

    IllegalArgumentException componentZero =
        assertThrows(IllegalArgumentException.class, () -> ComponentId.of(0L));
    assertEquals("ComponentId must be positive, got: 0", componentZero.getMessage());

    IllegalArgumentException componentNegative =
        assertThrows(IllegalArgumentException.class, () -> ComponentId.of(-1L));
    assertEquals("ComponentId must be positive, got: -1", componentNegative.getMessage());

    IllegalArgumentException systemZero =
        assertThrows(IllegalArgumentException.class, () -> SystemId.of(0L));
    assertEquals("SystemId must be positive, got: 0", systemZero.getMessage());

    IllegalArgumentException systemNegative =
        assertThrows(IllegalArgumentException.class, () -> SystemId.of(-1L));
    assertEquals("SystemId must be positive, got: -1", systemNegative.getMessage());
  }

  @Test
  void entityIdNoneSentinelExists() {
    assertEquals(EntityId.of(Long.MAX_VALUE), EntityId.NONE);
    assertNotEquals(EntityId.of(1L), EntityId.NONE);
  }

  @Test
  void systemIdOfNameIsConsistent() {
    assertEquals(SystemId.of("render"), SystemId.of("render"));
  }

  @Test
  void systemIdOfNameRejectsNullAndEmpty() {
    IllegalArgumentException nullName =
        assertThrows(IllegalArgumentException.class, () -> SystemId.of((String) null));
    assertEquals("System name must be non-null and non-empty", nullName.getMessage());

    IllegalArgumentException emptyName =
        assertThrows(IllegalArgumentException.class, () -> SystemId.of(""));
    assertEquals("System name must be non-null and non-empty", emptyName.getMessage());
  }
}
