package org.dynamisengine.core.version;

import java.util.Objects;

/**
 * Semantic version with major, minor, and patch components.
 *
 * @param major major version
 * @param minor minor version
 * @param patch patch version
 */
public record Version(int major, int minor, int patch) implements Comparable<Version> {
  public static final Version UNKNOWN = Version.of(0, 0, 0);

  /**
   * Creates a validated version.
   *
   * @param major major version
   * @param minor minor version
   * @param patch patch version
   * @return version instance
   */
  public static Version of(int major, int minor, int patch) {
    if (major < 0 || minor < 0 || patch < 0) {
      throw new IllegalArgumentException(
          "Version components must be non-negative: " + major + "." + minor + "." + patch);
    }
    return new Version(major, minor, patch);
  }

  /**
   * Parses a semantic version in {@code major.minor.patch} format.
   *
   * @param version version string
   * @return parsed version
   */
  public static Version parse(String version) {
    if (version == null) {
      throw new InvalidVersionException("null", "input must not be null");
    }
    if (version.isEmpty()) {
      throw new InvalidVersionException(version, "input must not be empty");
    }

    String[] parts = version.split("\\.", -1);
    if (parts.length != 3) {
      throw new InvalidVersionException(version, "expected format major.minor.patch");
    }

    int parsedMajor = parsePart(version, parts[0], "major");
    int parsedMinor = parsePart(version, parts[1], "minor");
    int parsedPatch = parsePart(version, parts[2], "patch");

    return Version.of(parsedMajor, parsedMinor, parsedPatch);
  }

  /**
   * Returns whether this version satisfies a required version.
   *
   * @param required required version
   * @return true when compatible
   */
  public boolean isCompatibleWith(Version required) {
    Objects.requireNonNull(required, "required must not be null");
    return this.major == required.major && this.minor >= required.minor;
  }

  /**
   * Returns whether this version is newer than another.
   *
   * @param other other version
   * @return true when newer
   */
  public boolean isNewerThan(Version other) {
    return compareTo(other) > 0;
  }

  /**
   * Returns whether this version is older than another.
   *
   * @param other other version
   * @return true when older
   */
  public boolean isOlderThan(Version other) {
    return compareTo(other) < 0;
  }

  @Override
  public int compareTo(Version other) {
    Objects.requireNonNull(other, "other must not be null");

    int majorCompare = Integer.compare(this.major, other.major);
    if (majorCompare != 0) {
      return majorCompare;
    }

    int minorCompare = Integer.compare(this.minor, other.minor);
    if (minorCompare != 0) {
      return minorCompare;
    }

    return Integer.compare(this.patch, other.patch);
  }

  @Override
  public String toString() {
    return major + "." + minor + "." + patch;
  }

  private static int parsePart(String input, String part, String componentName) {
    int value;
    try {
      value = Integer.parseInt(part);
    } catch (NumberFormatException ex) {
      throw new InvalidVersionException(
          input,
          "component '" + componentName + "' is not a non-negative integer: '" + part + "'");
    }

    if (value < 0) {
      throw new InvalidVersionException(
          input,
          "component '" + componentName + "' is not a non-negative integer: '" + part + "'");
    }

    return value;
  }
}
