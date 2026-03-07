package org.dynamisengine.core.lifecycle;

/**
 * Lifecycle contract implemented by every Dynamis subsystem.
 */
public interface DynamisSubsystem {
  /**
   * Called once at engine startup.
   *
   * @param ctx initialization context
   */
  void initialize(InitContext ctx);

  /**
   * Called on every engine tick.
   *
   * @param ctx tick context
   */
  void tick(TickContext ctx);

  /**
   * Called once at engine shutdown.
   *
   * <p>Implementations should handle failures internally and must not throw.
   */
  void shutdown();

  /**
   * Provides the default subsystem name for logging and registration.
   *
   * @return subsystem display name
   */
  default String subsystemName() {
    return getClass().getSimpleName();
  }
}
