package org.dynamis.core.resource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base type for resources requiring dispose-once semantics.
 */
public abstract class AbstractDisposable implements Disposable {
  private final AtomicBoolean disposed = new AtomicBoolean(false);
  private final AtomicBoolean disposing = new AtomicBoolean(false);

  /**
   * Performs cleanup logic.
   */
  protected abstract void onDispose();

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
      onDispose();
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
