package org.dynamis.core.lifecycle;

import java.util.Objects;
import org.dynamis.core.config.DynamisConfig;

/**
 * Immutable initialization data for a subsystem.
 *
 * @param config runtime configuration for subsystem initialization
 * @param contentProvider provider for content access
 * @param sessionProvider provider for session access
 */
public record InitContext(
    DynamisConfig config,
    Object contentProvider, // TODO: replace with DynamisContent in a future task.
    Object sessionProvider // TODO: replace with DynamisSession in a future task.
) {

  /**
   * Creates an initialization context without content or session providers.
   *
   * @param config runtime configuration
   * @return init context with null providers
   */
  public static InitContext of(DynamisConfig config) {
    return of(config, null, null);
  }

  /**
   * Creates a full initialization context.
   *
   * @param config runtime configuration
   * @param contentProvider content provider placeholder
   * @param sessionProvider session provider placeholder
   * @return init context instance
   */
  public static InitContext of(
      DynamisConfig config, Object contentProvider, Object sessionProvider) {
    return new InitContext(config, contentProvider, sessionProvider);
  }

  /**
   * Validates initialization context values.
   *
   * @param config runtime configuration
   * @param contentProvider content provider placeholder
   * @param sessionProvider session provider placeholder
   */
  public InitContext {
    Objects.requireNonNull(config, "config must not be null");
  }
}
