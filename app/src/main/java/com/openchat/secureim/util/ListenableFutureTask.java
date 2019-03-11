package com.openchat.secureim.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<V> extends FutureTask<V> {

  private final List<FutureTaskListener<V>> listeners = new LinkedList<>();

  public ListenableFutureTask(Callable<V> callable) {
    super(callable);
  }

  public synchronized void addListener(FutureTaskListener<V> listener) {
    if (this.isDone()) {
      callback(listener);
    } else {
      this.listeners.add(listener);
    }
  }

  public synchronized void removeListener(FutureTaskListener<V> listener) {
    this.listeners.remove(listener);
  }

  @Override
  protected synchronized void done() {
    callback();
  }

  private void callback() {
    for (FutureTaskListener<V> listener : listeners) {
      callback(listener);
    }
  }

  private void callback(FutureTaskListener<V> listener) {
    if (listener != null) {
      try {
        listener.onSuccess(get());
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      } catch (ExecutionException e) {
        listener.onFailure(e);
      }
    }
  }
}
