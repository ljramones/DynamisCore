package org.dynamisengine.core.resource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ResourceHandleTest {
  @Test
  void disposeQuietlySwallowsExceptions() {
    Disposable disposable =
        new Disposable() {
          @Override
          public void dispose() {
            throw new RuntimeException("boom");
          }

          @Override
          public boolean isDisposed() {
            return false;
          }
        };

    assertDoesNotThrow(disposable::disposeQuietly);
  }

  @Test
  void resourceHandleRejectsZeroHandle() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new TestResourceHandle(0L));
    assertEquals("Resource handle must be non-zero", exception.getMessage());
  }

  @Test
  void resourceHandleThrowsAfterDispose() {
    TestResourceHandle resource = new TestResourceHandle(7L);

    resource.dispose();

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, resource::resourceHandle);
    assertEquals("ResourceHandle has been disposed", exception.getMessage());
  }

  @Test
  void resourceHandleValidityTracksDisposal() {
    TestResourceHandle resource = new TestResourceHandle(5L);

    assertTrue(resource.isValid());

    resource.dispose();

    assertFalse(resource.isValid());
  }

  @Test
  void resourceHandleDisposeCallsReleaseExactlyOnce() {
    TestResourceHandle resource = new TestResourceHandle(11L);

    resource.dispose();

    assertEquals(1, resource.releaseCount.get());
  }

  @Test
  void resourceHandleDisposeIsIdempotent() {
    TestResourceHandle resource = new TestResourceHandle(12L);

    resource.dispose();
    resource.dispose();

    assertEquals(1, resource.releaseCount.get());
  }

  @Test
  void abstractDisposableCallsOnDisposeExactlyOnce() {
    TestDisposable disposable = new TestDisposable();

    disposable.dispose();

    assertEquals(1, disposable.disposeCount.get());
  }

  @Test
  void abstractDisposableIsIdempotent() {
    TestDisposable disposable = new TestDisposable();

    disposable.dispose();
    disposable.dispose();

    assertEquals(1, disposable.disposeCount.get());
  }

  @Test
  void resourceHandleConcurrentDisposeCallsReleaseOnce() throws InterruptedException {
    TestResourceHandle resource = new TestResourceHandle(21L);

    runConcurrentDispose(resource::dispose);

    assertEquals(1, resource.releaseCount.get());
  }

  @Test
  void abstractDisposableConcurrentDisposeCallsOnDisposeOnce() throws InterruptedException {
    TestDisposable disposable = new TestDisposable();

    runConcurrentDispose(disposable::dispose);

    assertEquals(1, disposable.disposeCount.get());
  }

  private static void runConcurrentDispose(ThrowingRunnable action) throws InterruptedException {
    CountDownLatch ready = new CountDownLatch(2);
    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(2);

    Thread first = new Thread(() -> invokeDispose(action, ready, start, done));
    Thread second = new Thread(() -> invokeDispose(action, ready, start, done));

    first.start();
    second.start();

    ready.await();
    start.countDown();
    done.await();
  }

  private static void invokeDispose(
      ThrowingRunnable action, CountDownLatch ready, CountDownLatch start, CountDownLatch done) {
    try {
      ready.countDown();
      start.await();
      action.run();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      done.countDown();
    }
  }

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run();
  }

  private static final class TestResourceHandle extends ResourceHandle {
    private final AtomicInteger releaseCount = new AtomicInteger(0);

    private TestResourceHandle(long resourceHandle) {
      super(resourceHandle);
    }

    @Override
    protected void releaseResourceHandle(long handle) {
      releaseCount.incrementAndGet();
    }
  }

  private static final class TestDisposable extends AbstractDisposable {
    private final AtomicInteger disposeCount = new AtomicInteger(0);

    @Override
    protected void onDispose() {
      disposeCount.incrementAndGet();
    }
  }
}
