package org.dynamis.core.version;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.dynamis.core.exception.DynamisException;
import org.junit.jupiter.api.Test;

class VersionTest {
  @Test
  void ofRejectsNegativeComponents() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> Version.of(1, -1, 0));
    assertEquals("Version components must be non-negative: 1.-1.0", ex.getMessage());
  }

  @Test
  void parseRoundTripsValidInput() {
    Version version = Version.parse("1.2.3");
    assertEquals(Version.of(1, 2, 3), version);
    assertEquals("1.2.3", version.toString());
  }

  @Test
  void parseRejectsInvalidFormats() {
    assertInvalid("null", () -> Version.parse(null));
    assertInvalid("", () -> Version.parse(""));
    assertInvalid("1.2", () -> Version.parse("1.2"));
    assertInvalid("1.2.3.4", () -> Version.parse("1.2.3.4"));
    assertInvalid("1.a.3", () -> Version.parse("1.a.3"));
    assertInvalid("1.-2.3", () -> Version.parse("1.-2.3"));
  }

  @Test
  void toStringUsesSemverFormat() {
    assertEquals("7.8.9", Version.of(7, 8, 9).toString());
  }

  @Test
  void compatibilityRulesAreApplied() {
    Version required = Version.of(1, 2, 0);

    assertTrue(Version.of(1, 3, 0).isCompatibleWith(required));
    assertFalse(Version.of(1, 1, 9).isCompatibleWith(required));
    assertFalse(Version.of(2, 2, 0).isCompatibleWith(required));
    assertTrue(Version.of(1, 2, 0).isCompatibleWith(required));
  }

  @Test
  void newerAndOlderComparisonsWork() {
    Version older = Version.of(1, 0, 0);
    Version newer = Version.of(1, 1, 0);

    assertTrue(newer.isNewerThan(older));
    assertTrue(older.isOlderThan(newer));
    assertFalse(newer.isOlderThan(older));
    assertFalse(older.isNewerThan(newer));
  }

  @Test
  void compareToProvidesTotalOrdering() {
    List<Version> versions = new ArrayList<>();
    versions.add(Version.of(2, 0, 0));
    versions.add(Version.of(1, 2, 3));
    versions.add(Version.of(1, 2, 2));
    versions.add(Version.of(1, 10, 0));

    versions.sort(Version::compareTo);

    assertEquals(List.of(
            Version.of(1, 2, 2),
            Version.of(1, 2, 3),
            Version.of(1, 10, 0),
            Version.of(2, 0, 0)),
        versions);
  }

  @Test
  void unknownVersionIsZeroZeroZero() {
    assertEquals("0.0.0", Version.UNKNOWN.toString());
  }

  @Test
  void componentVersionCompatibilityRequiresMatchingName() {
    ComponentVersion current = ComponentVersion.of("dynamis-core", Version.of(1, 3, 0));
    ComponentVersion requiredSame = ComponentVersion.of("dynamis-core", Version.of(1, 2, 0));
    ComponentVersion requiredOther = ComponentVersion.of("dynamis-physics", Version.of(1, 2, 0));

    assertTrue(current.isCompatibleWith(requiredSame));
    assertFalse(current.isCompatibleWith(requiredOther));
  }

  @Test
  void invalidVersionExceptionExtendsDynamisException() {
    InvalidVersionException ex = new InvalidVersionException("x", "bad");
    assertInstanceOf(DynamisException.class, ex);
  }

  private static void assertInvalid(String expectedInput, ThrowingRunnable action) {
    InvalidVersionException ex = assertThrows(InvalidVersionException.class, action::run);
    assertTrue(ex.getMessage().contains(expectedInput));
  }

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run();
  }
}
