package org.dynamisengine.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DynamisExceptionTest {
  @Test
  void messageConstructorSetsMessage() {
    DynamisException exception = new DynamisException("message");

    assertEquals("message", exception.getMessage());
  }

  @Test
  void messageAndCauseConstructorSetsMessageAndCause() {
    RuntimeException cause = new RuntimeException("cause");
    DynamisException exception = new DynamisException("message", cause);

    assertEquals("message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
  void causeConstructorPreservesCauseChain() {
    RuntimeException cause = new RuntimeException("cause");
    DynamisException exception = new DynamisException(cause);

    assertEquals(cause, exception.getCause());
    assertTrue(exception.getMessage().contains("cause"));
  }

  @Test
  void fullConstructorSupportsSuppressionAndStackTraceFlags() {
    RuntimeException cause = new RuntimeException("cause");
    DynamisException exception =
        new DynamisException("message", cause, true, true);

    assertEquals("message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
  void baseComponentAndRecoverableDefaults() {
    DynamisException exception = new DynamisException("message");

    assertEquals("dynamis-core", exception.component());
    assertFalse(exception.isRecoverable());
  }
}
