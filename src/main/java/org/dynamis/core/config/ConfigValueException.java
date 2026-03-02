package org.dynamis.core.config;

import org.dynamis.core.exception.DynamisException;

/**
 * Thrown when a configuration value cannot be parsed to the requested type.
 */
public final class ConfigValueException extends DynamisException {
  /**
   * Creates an invalid-value exception.
   *
   * @param key configuration key
   * @param value raw value string
   * @param targetType target type name
   */
  public ConfigValueException(String key, String value, String targetType) {
    super(
        "Invalid value for key '"
            + key
            + "': '"
            + value
            + "' is not a valid "
            + targetType);
  }
}
