package com.beust;

import com.google.common.base.Objects;
import com.google.common.base.Ticker;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A map that returns keys that match two conditions: the key hasn't been used
 * more than maxEntries times within the last periodMillis seconds.
 * 
 * @author Cedric Beust <cedric@beust.com>
 * @since Sep 1, 2012
 */
public class SlidingWindowMap<T> {
  private Collection<T> keys;
  private long periodMillis;
  private long maxEntries;
  private Map<T, List<Entry>> map = Maps.newHashMap();
  private Ticker ticker;

  class Entry {
    long millis;

    boolean isWithinPeriod() {
      long now = now();
      boolean result = now - millis < periodMillis;
      return result;
//      return now() - millis < periodMillis;
    }
  }

  public SlidingWindowMap(Collection<T> keys, long periodMillis, long maxEntries) {
    init(keys, periodMillis, maxEntries, Ticker.systemTicker());
  }

  public SlidingWindowMap(Collection<T> keys, long periodMillis, long maxEntries, Ticker ticker) {
    init(keys, periodMillis, maxEntries, ticker);
  }

  private void init(Collection<T> keys, long periodMillis, long maxEntries, Ticker ticker) {
    this.keys = keys;
    this.periodMillis = periodMillis;
    this.maxEntries = maxEntries;
    this.ticker = ticker;

    for (T key : keys) {
      List<Entry> l = Lists.newArrayList();
      map.put(key, l);
    }
  }

  private void doMaintenance() {
    // clean up expired entries
    for (T key : map.keySet()) {
      List<Entry> entries = map.get(key);
      for (Iterator<Entry> it = entries.iterator(); it.hasNext();) {
        Entry e = it.next();
        if (!e.isWithinPeriod()) {
          p("Removing entry for " + key + ", new size:" + entries.size());
          it.remove();
        }
      }
    }
  }

  class KeyEntry implements Comparable<KeyEntry> {
    T key;
    int entrySize;

    @Override
    public int compareTo(KeyEntry arg0) {
      return entrySize - arg0.entrySize;
    }
    @Override
    public String toString() {
      return Objects.toStringHelper("KeyEntry")
          .add("key", key)
          .add("size", entrySize)
          .toString();
    }
  }

  /**
   * @return the next allowed key (hasn't been less than maxEntries time withing
   *         a period of periodMillis or null if no such key is available).
   */
  @Nullable
  synchronized public T getNextKey(boolean block) {
    doMaintenance();

    // Return keys that have the smallest entry list first
    List<KeyEntry> keyEntries = Lists.newArrayList();
    for (T key : keys) {
      List<Entry> e = map.get(key);
      KeyEntry ke = new KeyEntry();
      ke.key = key;
      ke.entrySize = e.size();
      keyEntries.add(ke);
    }
    Collections.sort(keyEntries);

    for (KeyEntry ke : keyEntries) {
      T key = ke.key;
      List<Entry> entries = map.get(key);
      p("Checking if key " + key + " is available, size:" + entries.size());
      if (entries.isEmpty()
          || (entries.size() < maxEntries && entries.get(0).isWithinPeriod())) {
        return newEntryAndReturnKey(key, entries);
      }
    }

    // No key is available, find the oldest entry and block until it expires
    if (block) {
      Entry oldestEntry = null;
      for (Map.Entry<T, List<Entry>> es : map.entrySet()) {
        List<Entry> list = es.getValue();
        if (!list.isEmpty()) {
          Entry entry = list.get(0);
          // Theoretically, all the entries should be within the period
          if (entry.isWithinPeriod()) {
            if (oldestEntry == null || entry.millis < oldestEntry.millis) {
              oldestEntry = entry;
            }
          }
        }
      }

      // Example: period = 10mn, now = 3:05, old entry = 3:02
      // now - oldest = 3mn
      if (oldestEntry != null) {
        long now = now();
        System.out.println("now:" + now/1000 + " oldest:" + oldestEntry.millis/1000
            + " now-oldest:" + (now - oldestEntry.millis));
        long timeToWaitMs = periodMillis - (now - oldestEntry.millis);
        try {
          p("Sleeping " + (timeToWaitMs / 1000) + " seconds");
          Thread.sleep(timeToWaitMs + 1000 /* add a second */);
          return getNextKey(false /* don't block */);
        } catch (InterruptedException e) {
          // ignore
        }
      }
    }
    return null;
  }

  private long now() {
    return System.currentTimeMillis();
//    return ticker.read() * 1000;
  }

  private T newEntryAndReturnKey(T key, List<Entry> entries) {
    Entry newEntry = new Entry();
    newEntry.millis = now();
    entries.add(newEntry);
    p("Creating new entry for " + key + ", now:" + newEntry.millis + " new size: " + entries.size());
    return key;
  }

  private static void p(String s) {
    System.out.println("[SlidingWindowMap] " + s);
  }
}
