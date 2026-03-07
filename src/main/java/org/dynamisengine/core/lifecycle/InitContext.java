package org.dynamisengine.core.lifecycle;

import java.util.Objects;
import org.dynamisengine.core.config.DynamisConfig;

/**
 * Immutable initialization data for a subsystem.
 *
 * @param config runtime configuration for subsystem initialization
 */
public record InitContext(DynamisConfig config) {

  /**
   * Creates an initialization context.
   *
   * @param config runtime configuration
   * @return init context
   */
  public static InitContext of(DynamisConfig config) {
    return new InitContext(config);
  }

  /**
   * Validates initialization context values.
   *
   * @param config runtime configuration
   */
  public InitContext {
    Objects.requireNonNull(config, "config must not be null");
  }
}
