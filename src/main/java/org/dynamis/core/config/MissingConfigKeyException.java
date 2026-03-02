package org.dynamis.core.config;

import org.dynamis.core.exception.DynamisException;

/**
 * Thrown when a required configuration key is missing.
 */
public final class MissingConfigKeyException extends DynamisException {
  /**
   * Creates a missing-key exception.
   *
   * @param key missing configuration key
   */
  public MissingConfigKeyException(String key) {
    super("Required config key not found: " + key);
  }
}
