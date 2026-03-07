package org.dynamisengine.core.version;

import org.dynamisengine.core.exception.DynamisException;

/**
 * Thrown when a version string cannot be parsed as a valid semantic version.
 */
public final class InvalidVersionException extends DynamisException {
  /**
   * Creates an invalid-version exception.
   *
   * @param input invalid input
   * @param reason parse/validation reason
   */
  public InvalidVersionException(String input, String reason) {
    super("Invalid version string '" + input + "': " + reason);
  }
}
