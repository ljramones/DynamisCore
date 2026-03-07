package org.dynamisengine.core.resource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base type for externally-backed resources that own a resource handle.
 */
public abstract class ResourceHandle implements Disposable {
  private final long resourceHandle;
  private final AtomicBoolean disposed = new AtomicBoolean(false);
  private final AtomicBoolean disposing = new AtomicBoolean(false);

  /**
   * Creates a resource handle wrapper.
   *
   * @param resourceHandle non-zero resource handle
   */
  protected ResourceHandle(long resourceHandle) {
    if (resourceHandle == 0L) {
      throw new IllegalArgumentException("Resource handle must be non-zero");
    }
    this.resourceHandle = resourceHandle;
  }

  /**
   * Returns the resource handle while valid.
   *
   * @return resource handle
   */
  public long resourceHandle() {
    if (isDisposed()) {
      throw new IllegalStateException("ResourceHandle has been disposed");
    }
    return resourceHandle;
  }

  /**
   * Returns whether the underlying resource is still valid.
   *
   * @return true when not disposed
   */
  public boolean isValid() {
    return !isDisposed();
  }

  /**
   * Releases the wrapped resource handle.
   *
   * @param handle resource handle value
   */
  protected abstract void releaseResourceHandle(long handle);

  @Override
  public void dispose() {
    if (disposed.get()) {
      return;
    }
    if (!disposing.compareAndSet(false, true)) {
      while (disposing.get() && !disposed.get()) {
        Thread.onSpinWait();
      }
      return;
    }

    try {
      if (disposed.get()) {
        return;
      }
      releaseResourceHandle(resourceHandle);
      disposed.set(true);
    } finally {
      disposing.set(false);
    }
  }

  @Override
  public boolean isDisposed() {
    return disposed.get();
  }
}
