package org.dynamis.core.lifecycle;

/**
 * Lifecycle state for subsystem execution.
 */
public enum SubsystemState {
  UNINITIALIZED,
  INITIALIZING,
  RUNNING,
  SHUTTING_DOWN,
  SHUTDOWN,
  FAILED
}
