package org.dynamis.core.native_;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class NativeResourceTest {
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
  void nativeResourceRejectsZeroHandle() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new TestNativeResource(0L));
    assertEquals("Native handle must be non-zero", exception.getMessage());
  }

  @Test
  void nativeHandleThrowsAfterDispose() {
    TestNativeResource resource = new TestNativeResource(7L);

    resource.dispose();

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, resource::nativeHandle);
    assertEquals("NativeResource has been disposed", exception.getMessage());
  }

  @Test
  void nativeResourceValidityTracksDisposal() {
    TestNativeResource resource = new TestNativeResource(5L);

    assertTrue(resource.isValid());

    resource.dispose();

    assertFalse(resource.isValid());
  }

  @Test
  void nativeResourceDisposeCallsReleaseExactlyOnce() {
    TestNativeResource resource = new TestNativeResource(11L);

    resource.dispose();

    assertEquals(1, resource.releaseCount.get());
  }

  @Test
  void nativeResourceDisposeIsIdempotent() {
    TestNativeResource resource = new TestNativeResource(12L);

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
  void nativeResourceConcurrentDisposeCallsReleaseOnce() throws InterruptedException {
    TestNativeResource resource = new TestNativeResource(21L);

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

  private static final class TestNativeResource extends NativeResource {
    private final AtomicInteger releaseCount = new AtomicInteger(0);

    private TestNativeResource(long nativeHandle) {
      super(nativeHandle);
    }

    @Override
    protected void releaseNativeHandle(long handle) {
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
