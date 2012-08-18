package com.beust.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

public class PendingCache<K, V> {

  private Cache<K, V> cache;
  private Set<K> pendingKeys = Sets.newHashSet();
  private ConcurrentMap<K, List<Object>> waitingThreads = Maps.newConcurrentMap();

  public PendingCache() {
    p("Creating PendingCache");
    this.cache = CacheBuilder.newBuilder().build();
  }

  private static void p(String string) {
    System.out.println(Thread.currentThread().getId() + " [PendingCache] " + string);
  }

  public V get(final K key, Callable<V> callable) throws ExecutionException {
    p("get() on key:" + key + " pending:" + pendingKeys
        + " inCache:" + (cache.getIfPresent(key) != null));
    V result = cache.getIfPresent(key);
    if (result != null) {
      return result;
    }

    boolean waiting = false;
    if (isPending(key)) {
      synchronized(this) {
        try {
          p("Waiting...");
          waiting = true;
          addToWaiting(key, this);
          wait();
          p("... waiting over");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    } else {
      addToPending(key);
    }

    // Load the cache with the callable
    result = cache.get(key, callable);
    if (! waiting) {
      wakeUpThreads(key);
    }
    removeFromPending(key);
    return result;
  }

  private void wakeUpThreads(K key) {
    synchronized(waitingThreads) {
      List<Object> objects = waitingThreads.get(key);
      if (objects != null) {
        p("Notifying " + objects.size() + " threads for key " + key);
        for (Object object : objects) {
          synchronized(object) {
            object.notify();
          }
        }
        waitingThreads.remove(key);
      } else {
        p("No threads to notify");
      }
    }
  }

  synchronized private void addToPending(K key) {
    if (pendingKeys.contains(key)) {
      System.out.println("Adding to pending twice");
      throw new IllegalArgumentException("Should not happen");
    }
    pendingKeys.add(key);
  }

  synchronized private void removeFromPending(K key) {
    pendingKeys.remove(key);
  }

  synchronized private boolean isPending(K key) {
    return pendingKeys.contains(key);
  }

  private void addToWaiting(K key, Object object) {
    synchronized(waitingThreads) {
      List<Object> l = waitingThreads.get(key);
      if (l == null) {
        l = Lists.newArrayList();
        waitingThreads.put(key, l);
      }
      l.add(object);
    }
  }
}
