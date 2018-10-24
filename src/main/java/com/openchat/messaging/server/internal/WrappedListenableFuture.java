package com.openchat.messaging.server.internal;


import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WrappedListenableFuture<V> implements ListenableFuture<V> {

  private final com.ning.http.client.ListenableFuture<V> wrapped;

  public WrappedListenableFuture(com.ning.http.client.ListenableFuture<V> wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void addListener(Runnable runnable, Executor executor) {
    wrapped.addListener(runnable, executor);
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return wrapped.cancel(mayInterruptIfRunning);
  }

  @Override
  public boolean isCancelled() {
    return wrapped.isCancelled();
  }

  @Override
  public boolean isDone() {
    return wrapped.isDone();
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    return wrapped.get();
  }

  @Override
  public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return wrapped.get(timeout, unit);
  }
}
