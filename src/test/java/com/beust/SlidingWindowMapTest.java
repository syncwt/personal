package com.beust;

import com.google.common.base.Ticker;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SlidingWindowMapTest {
  private final static List<String> KEYS = ImmutableList.of("a", "b", "c", "d");
  private static final int MAX_ENTRIES = 50;
  private static final long PERIOD_MS = 10000;

  private static final long SLEEP_TIME_MS = 10;
  private static final long CALLS = 1000;
  

  private Ticker ticker = new FastTicker();
  private Map<String, List<Entry>> map = Maps.newHashMap();
  private SlidingWindowMap<String> swm = new SlidingWindowMap<String>(KEYS, PERIOD_MS, MAX_ENTRIES);
//      ticker);

  private long now() {
    return ticker.read() * 1000;
  }

  class Entry {
    long millis;

    boolean isWithinPeriod() {
      return now() - millis < PERIOD_MS;
    }
  }

  @BeforeClass
  public void bc() {
    for (String k : KEYS) {
      map.put(k, Lists.<Entry>newArrayList());
    }
  }

  @Test
  public void t1() throws InterruptedException {
    Multimap<String, String> mm = ArrayListMultimap.create();
    for (int i = 0; i < CALLS; i++) {
      String k = swm.getNextKey(true);
      addKey(k);
      System.out.println("Received key " + k + " " + map.get(k).size());
      mm.put(k, k);
      Assert.assertTrue(isValid(k));
      Thread.sleep(SLEEP_TIME_MS);
    }
    System.out.println("Distribution:");
    for (String k : mm.keySet()) {
      System.out.println(k + ":" + mm.get(k).size());
      
    }
//    System.out.println(map);
  }

  private void addKey(String k) {
    // clean up
    for (Map.Entry<String, List<Entry>> es : map.entrySet()) {
      for (Iterator<Entry> it = es.getValue().iterator(); it.hasNext(); ) {
        Entry e = it.next();
        if (e.millis + PERIOD_MS < now()) {
          it.remove();
        }
      }
    }

    // add
    List<Entry> l = map.get(k);
    Entry entry = new Entry();
    entry.millis = now();
    l.add(entry);
  }

  private boolean isValid(String k) {
    List<Entry> l = map.get(k);
    return l.size() < MAX_ENTRIES || l.get(0).millis + PERIOD_MS < now();
  }
}
