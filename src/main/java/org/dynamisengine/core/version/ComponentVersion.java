package org.dynamisengine.core.version;

import java.util.Objects;

/**
 * Named component version pairing.
 *
 * @param componentName component name
 * @param version component version
 */
public record ComponentVersion(String componentName, Version version) {
  /**
   * Creates a validated component-version pair.
   *
   * @param componentName component name
   * @param version component version
   * @return component version instance
   */
  public static ComponentVersion of(String componentName, Version version) {
    return new ComponentVersion(componentName, version);
  }

  /**
   * Validates component-version values.
   *
   * @param componentName component name
   * @param version component version
   */
  public ComponentVersion {
    if (componentName == null || componentName.isEmpty()) {
      throw new IllegalArgumentException("componentName must be non-null and non-empty");
    }
    Objects.requireNonNull(version, "version must not be null");
  }

  /**
   * Returns whether this component version is compatible with a required component version.
   *
   * @param required required component version
   * @return true when component name matches and version is compatible
   */
  public boolean isCompatibleWith(ComponentVersion required) {
    Objects.requireNonNull(required, "required must not be null");
    return this.componentName.equals(required.componentName)
        && this.version.isCompatibleWith(required.version);
  }
}
