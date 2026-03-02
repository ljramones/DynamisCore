package org.dynamis.core.logging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;

class DynamisLoggerTest {
  @Test
  void getByClassUsesClassName() {
    DynamisLogger logger = DynamisLogger.get(DynamisLoggerTest.class);
    assertEquals(DynamisLoggerTest.class.getName(), logger.name());
  }

  @Test
  void getByNameUsesProvidedName() {
    DynamisLogger logger = DynamisLogger.get("org.dynamis.test.logger");
    assertEquals("org.dynamis.test.logger", logger.name());
  }

  @Test
  void getByNameRejectsNullAndEmpty() {
    IllegalArgumentException nullName =
        assertThrows(IllegalArgumentException.class, () -> DynamisLogger.get((String) null));
    assertEquals("Logger name must be non-null and non-empty", nullName.getMessage());

    IllegalArgumentException emptyName =
        assertThrows(IllegalArgumentException.class, () -> DynamisLogger.get(""));
    assertEquals("Logger name must be non-null and non-empty", emptyName.getMessage());
  }

  @Test
  void logLevelThresholdBehaviorMatchesOrdering() {
    assertTrue(LogLevel.TRACE.isEnabledAt(LogLevel.TRACE));
    assertFalse(LogLevel.TRACE.isEnabledAt(LogLevel.DEBUG));
  }

  @Test
  void isEnabledReflectsCurrentLoggerLevel() {
    DynamisLogger logger = DynamisLogger.get("org.dynamis.test.enabled.1");
    logger.setLevel(LogLevel.WARN);

    assertFalse(logger.isEnabled(LogLevel.INFO));
    assertTrue(logger.isEnabled(LogLevel.WARN));
    assertTrue(logger.isEnabled(LogLevel.ERROR));
  }

  @Test
  void setLevelChangesEffectiveLevel() {
    DynamisLogger logger = DynamisLogger.get("org.dynamis.test.enabled.2");
    logger.setLevel(LogLevel.ERROR);
    assertFalse(logger.isEnabled(LogLevel.WARN));

    logger.setLevel(LogLevel.DEBUG);
    assertTrue(logger.isEnabled(LogLevel.DEBUG));
  }

  @Test
  void allLoggingMethodsAreCallable() {
    DynamisLogger logger = DynamisLogger.get("org.dynamis.test.methods");
    RuntimeException cause = new RuntimeException("boom");

    assertDoesNotThrow(() -> logger.trace("trace"));
    assertDoesNotThrow(() -> logger.trace("trace", cause));
    assertDoesNotThrow(() -> logger.debug("debug"));
    assertDoesNotThrow(() -> logger.debug("debug", cause));
    assertDoesNotThrow(() -> logger.info("info"));
    assertDoesNotThrow(() -> logger.info("info", cause));
    assertDoesNotThrow(() -> logger.warn("warn"));
    assertDoesNotThrow(() -> logger.warn("warn", cause));
    assertDoesNotThrow(() -> logger.error("error"));
    assertDoesNotThrow(() -> logger.error("error", cause));
  }

  @Test
  void logRecordFactoriesPopulateFields() {
    RuntimeException cause = new RuntimeException("cause");
    LogRecord record = LogRecord.of(LogLevel.INFO, "logger", "message", cause);

    assertEquals(LogLevel.INFO, record.level());
    assertEquals("logger", record.loggerName());
    assertEquals("message", record.message());
    assertEquals("cause", record.cause().getMessage());
    assertTrue(record.timestampNanos() > 0L);
  }

  @Test
  void logRecordWithoutCauseIsValid() {
    AtomicLong timestamp = new AtomicLong();
    LogRecord record = LogRecord.of(LogLevel.DEBUG, "logger", "message");
    timestamp.set(record.timestampNanos());

    assertEquals(LogLevel.DEBUG, record.level());
    assertEquals("logger", record.loggerName());
    assertEquals("message", record.message());
    assertNull(record.cause());
    assertTrue(timestamp.get() > 0L);
  }
}
