package org.dynamis.core.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DynamisConfigTest {
  @Test
  void builderRoundTripsAllValueTypes() {
    DynamisConfig config =
        DynamisConfig.builder()
            .set("name", "dynamis")
            .set("threads", 4)
            .set("scale", 1.5d)
            .set("enabled", true)
            .build();

    assertEquals("dynamis", config.getString("name"));
    assertEquals(4, config.getInt("threads"));
    assertEquals(1.5d, config.getDouble("scale"));
    assertTrue(config.getBoolean("enabled"));
  }

  @Test
  void builderRejectsInvalidKeys() {
    IllegalArgumentException nullKey =
        assertThrows(IllegalArgumentException.class, () -> DynamisConfig.builder().set(null, "x"));
    assertEquals(
        "Config key must be non-null, non-empty, and contain no whitespace: null",
        nullKey.getMessage());

    IllegalArgumentException emptyKey =
        assertThrows(IllegalArgumentException.class, () -> DynamisConfig.builder().set("", "x"));
    assertEquals(
        "Config key must be non-null, non-empty, and contain no whitespace: ",
        emptyKey.getMessage());

    IllegalArgumentException whitespaceKey =
        assertThrows(
            IllegalArgumentException.class, () -> DynamisConfig.builder().set("a b", "x"));
    assertEquals(
        "Config key must be non-null, non-empty, and contain no whitespace: a b",
        whitespaceKey.getMessage());
  }

  @Test
  void builderRejectsNullStringValue() {
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> DynamisConfig.builder().set("name", (String) null));
    assertEquals("Config value must not be null for key: name", ex.getMessage());
  }

  @Test
  void getStringThrowsMissingConfigKeyExceptionWhenAbsent() {
    DynamisConfig config = DynamisConfig.empty();

    MissingConfigKeyException ex =
        assertThrows(MissingConfigKeyException.class, () -> config.getString("missing"));
    assertTrue(ex.getMessage().contains("missing"));
  }

  @Test
  void getStringWithDefaultReturnsDefaultWhenAbsent() {
    DynamisConfig config = DynamisConfig.empty();

    assertEquals("fallback", config.getString("missing", "fallback"));
  }

  @Test
  void getIntThrowsConfigValueExceptionForNonInteger() {
    DynamisConfig config = DynamisConfig.builder().set("threads", "four").build();

    ConfigValueException ex =
        assertThrows(ConfigValueException.class, () -> config.getInt("threads"));
    assertTrue(ex.getMessage().contains("threads"));
    assertTrue(ex.getMessage().contains("int"));
  }

  @Test
  void getBooleanParsesOnlyTrueCaseInsensitiveAsTrue() {
    DynamisConfig config =
        DynamisConfig.builder()
            .set("a", "true")
            .set("b", "TRUE")
            .set("c", "True")
            .set("d", "false")
            .set("e", "yes")
            .set("f", "1")
            .build();

    assertTrue(config.getBoolean("a"));
    assertTrue(config.getBoolean("b"));
    assertTrue(config.getBoolean("c"));
    assertFalse(config.getBoolean("d"));
    assertFalse(config.getBoolean("e"));
    assertFalse(config.getBoolean("f"));
  }

  @Test
  void containsKeyAndSizeReflectBuiltEntries() {
    DynamisConfig config =
        DynamisConfig.builder().set("alpha", "1").set("beta", 2).set("gamma", false).build();

    assertEquals(3, config.size());
    assertTrue(config.containsKey("alpha"));
    assertFalse(config.containsKey("delta"));
  }

  @Test
  void emptyHasZeroEntries() {
    assertEquals(0, DynamisConfig.empty().size());
  }
}
