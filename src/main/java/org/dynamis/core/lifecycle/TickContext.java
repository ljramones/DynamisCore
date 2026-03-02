package org.dynamis.core.lifecycle;

/**
 * Immutable tick timing and progression metadata.
 *
 * @param tickNumber zero-based tick counter
 * @param deltaTime seconds elapsed since previous tick
 * @param elapsedTime cumulative elapsed seconds
 */
public record TickContext(long tickNumber, double deltaTime, double elapsedTime) {
  /**
   * Creates a validated tick context.
   *
   * @param tickNumber zero-based tick counter
   * @param deltaTime seconds elapsed since previous tick
   * @param elapsedTime cumulative elapsed seconds
   * @return tick context instance
   */
  public static TickContext of(long tickNumber, double deltaTime, double elapsedTime) {
    return new TickContext(tickNumber, deltaTime, elapsedTime);
  }

  /**
   * Validates tick context values.
   *
   * @param tickNumber zero-based tick counter
   * @param deltaTime seconds elapsed since previous tick
   * @param elapsedTime cumulative elapsed seconds
   */
  public TickContext {
    if (tickNumber < 0L) {
      throw new IllegalArgumentException("tickNumber must be >= 0, got: " + tickNumber);
    }
    if (deltaTime < 0.0d) {
      throw new IllegalArgumentException("deltaTime must be >= 0.0, got: " + deltaTime);
    }
    if (elapsedTime < 0.0d) {
      throw new IllegalArgumentException("elapsedTime must be >= 0.0, got: " + elapsedTime);
    }
  }

  /**
   * Returns whether this context represents the first engine tick.
   *
   * @return true when {@code tickNumber == 0}
   */
  public boolean isFirstTick() {
    return tickNumber == 0L;
  }
}
