package org.dynamisengine.core.resource;

/**
 * Contract for resources that require explicit cleanup.
 */
public interface Disposable {
  /**
   * Releases held resources.
   */
  void dispose();

  /**
   * Returns whether this resource has been disposed.
   *
   * @return true when disposed
   */
  boolean isDisposed();

  /**
   * Attempts disposal and suppresses any thrown exception.
   */
  default void disposeQuietly() {
    try {
      dispose();
    } catch (Exception e) {
      // Intentional no-op: callers opting into quiet disposal accept swallowed exceptions.
    }
  }
}
