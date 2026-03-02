package org.dynamis.core.native_;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base type for JNI-backed resources that own a native handle.
 */
public abstract class NativeResource implements Disposable {
  private final long nativeHandle;
  private final AtomicBoolean disposed = new AtomicBoolean(false);
  private final AtomicBoolean disposing = new AtomicBoolean(false);

  /**
   * Creates a native resource wrapper.
   *
   * @param nativeHandle non-zero native handle
   */
  protected NativeResource(long nativeHandle) {
    if (nativeHandle == 0L) {
      throw new IllegalArgumentException("Native handle must be non-zero");
    }
    this.nativeHandle = nativeHandle;
  }

  /**
   * Returns the native handle while valid.
   *
   * @return native handle
   */
  public long nativeHandle() {
    if (isDisposed()) {
      throw new IllegalStateException("NativeResource has been disposed");
    }
    return nativeHandle;
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
   * Releases the wrapped native handle.
   *
   * @param handle native handle value
   */
  protected abstract void releaseNativeHandle(long handle);

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
      releaseNativeHandle(nativeHandle);
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
