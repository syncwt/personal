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

public class PendingCache {

  private Cache<String, String> cache;
  private Set<String> pendingKeys = Sets.newHashSet();
  private ConcurrentMap<String, List<Object>> waitingThreads = Maps.newConcurrentMap();

  public PendingCache() {
    p("Creating WaitCache");
    this.cache = CacheBuilder.newBuilder().build();
  }

  private static void p(String string) {
    System.out.println(Thread.currentThread().getId() + " [PendingCache] " + string);
  }

  public String get(final String key, Callable<String> callable) throws ExecutionException {
    p("get() on key:" + key + " pending:" + pendingKeys
        + " inCache:" + (cache.getIfPresent(key) != null));
    String result = cache.getIfPresent(key);
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

  private void wakeUpThreads(String url) {
    synchronized(waitingThreads) {
      List<Object> objects = waitingThreads.get(url);
      if (objects != null) {
        p("Notifying " + objects.size() + " threads");
        for (Object object : objects) {
          synchronized(object) {
            object.notify();
          }
        }
        waitingThreads.remove(url);
      } else {
        p("No threads to notify");
      }
    }
  }

  synchronized private void addToPending(String url) {
    if (pendingKeys.contains(url)) {
      System.out.println("Adding to pending twice");
      throw new IllegalArgumentException("Should not happen");
    }
    pendingKeys.add(url);
  }

  synchronized private void removeFromPending(String url) {
    pendingKeys.remove(url);
  }

  synchronized private boolean isPending(String url) {
    return pendingKeys.contains(url);
  }

  private void addToWaiting(String url, Object object) {
    synchronized(waitingThreads) {
      List<Object> l = waitingThreads.get(url);
      if (l == null) {
        l = Lists.newArrayList();
        waitingThreads.put(url, l);
      }
      l.add(object);
    }
  }
}
