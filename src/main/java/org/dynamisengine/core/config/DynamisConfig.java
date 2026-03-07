package org.dynamisengine.core.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Immutable configuration container backed by string key-value pairs.
 */
public final class DynamisConfig {
  private static final String INVALID_KEY_MESSAGE_PREFIX =
      "Config key must be non-null, non-empty, and contain no whitespace: ";

  private final Map<String, String> values;

  private DynamisConfig(Map<String, String> values) {
    this.values = values;
  }

  /**
   * Creates an empty configuration.
   *
   * @return empty config
   */
  public static DynamisConfig empty() {
    return new DynamisConfig(Collections.unmodifiableMap(new LinkedHashMap<>()));
  }

  /**
   * Creates a new configuration builder.
   *
   * @return builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Gets a required string value.
   *
   * @param key config key
   * @return string value
   */
  public String getString(String key) {
    validateKey(key);
    String value = values.get(key);
    if (value == null) {
      throw new MissingConfigKeyException(key);
    }
    return value;
  }

  /**
   * Gets a string value with fallback.
   *
   * @param key config key
   * @param defaultValue fallback value when key is absent
   * @return config value or default
   */
  public String getString(String key, String defaultValue) {
    validateKey(key);
    return values.getOrDefault(key, defaultValue);
  }

  /**
   * Gets a required int value.
   *
   * @param key config key
   * @return parsed int value
   */
  public int getInt(String key) {
    String value = getString(key);
    return parseInt(key, value);
  }

  /**
   * Gets an int value with fallback.
   *
   * @param key config key
   * @param defaultValue fallback value when key is absent
   * @return parsed int value or fallback
   */
  public int getInt(String key, int defaultValue) {
    validateKey(key);
    String value = values.get(key);
    if (value == null) {
      return defaultValue;
    }
    return parseInt(key, value);
  }

  /**
   * Gets a required double value.
   *
   * @param key config key
   * @return parsed double value
   */
  public double getDouble(String key) {
    String value = getString(key);
    return parseDouble(key, value);
  }

  /**
   * Gets a double value with fallback.
   *
   * @param key config key
   * @param defaultValue fallback value when key is absent
   * @return parsed double value or fallback
   */
  public double getDouble(String key, double defaultValue) {
    validateKey(key);
    String value = values.get(key);
    if (value == null) {
      return defaultValue;
    }
    return parseDouble(key, value);
  }

  /**
   * Gets a required boolean value.
   *
   * @param key config key
   * @return parsed boolean value
   */
  public boolean getBoolean(String key) {
    String value = getString(key);
    return parseBoolean(value);
  }

  /**
   * Gets a boolean value with fallback.
   *
   * @param key config key
   * @param defaultValue fallback value when key is absent
   * @return parsed boolean value or fallback
   */
  public boolean getBoolean(String key, boolean defaultValue) {
    validateKey(key);
    String value = values.get(key);
    if (value == null) {
      return defaultValue;
    }
    return parseBoolean(value);
  }

  /**
   * Returns whether a key is present.
   *
   * @param key config key
   * @return true when present
   */
  public boolean containsKey(String key) {
    validateKey(key);
    return values.containsKey(key);
  }

  /**
   * Returns number of entries.
   *
   * @return entry count
   */
  public int size() {
    return values.size();
  }

  private static int parseInt(String key, String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException ex) {
      throw new ConfigValueException(key, value, "int");
    }
  }

  private static double parseDouble(String key, String value) {
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException ex) {
      throw new ConfigValueException(key, value, "double");
    }
  }

  private static boolean parseBoolean(String value) {
    return "true".equalsIgnoreCase(value);
  }

  private static void validateKey(String key) {
    if (key == null || key.isEmpty() || containsWhitespace(key)) {
      throw new IllegalArgumentException(INVALID_KEY_MESSAGE_PREFIX + key);
    }
  }

  private static boolean containsWhitespace(String key) {
    for (int i = 0; i < key.length(); i++) {
      if (Character.isWhitespace(key.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Builder for immutable {@link DynamisConfig} instances.
   */
  public static final class Builder {
    private final Map<String, String> entries = new LinkedHashMap<>();

    /**
     * Sets a string value.
     *
     * @param key config key
     * @param value config value
     * @return builder
     */
    public Builder set(String key, String value) {
      validateKey(key);
      validateValue(key, value);
      entries.put(key, value);
      return this;
    }

    /**
     * Sets an int value.
     *
     * @param key config key
     * @param value config value
     * @return builder
     */
    public Builder set(String key, int value) {
      return set(key, String.valueOf(value));
    }

    /**
     * Sets a double value.
     *
     * @param key config key
     * @param value config value
     * @return builder
     */
    public Builder set(String key, double value) {
      return set(key, String.valueOf(value));
    }

    /**
     * Sets a boolean value.
     *
     * @param key config key
     * @param value config value
     * @return builder
     */
    public Builder set(String key, boolean value) {
      return set(key, String.valueOf(value));
    }

    /**
     * Builds an immutable config snapshot.
     *
     * @return immutable config
     */
    public DynamisConfig build() {
      return new DynamisConfig(Collections.unmodifiableMap(new LinkedHashMap<>(entries)));
    }

    private static void validateValue(String key, String value) {
      if (value == null) {
        throw new IllegalArgumentException("Config value must not be null for key: " + key);
      }
    }
  }
}
