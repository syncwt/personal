package com.beust;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A map that returns keys that match two conditions: the key hasn't been used more than
 * maxEntries times within the last periodMillis seconds.
 * 
 * @author Cedric Beust <cedric@beust.com>
 * @since Sep 1, 2012
 */
public class SlidingWindowMap<T> {
    private final Collection<T> keys;
    private final long periodMillis;
    private final long maxEntries;
    class Entry {
        long millis;
        boolean isWithinPeriod() {
            return now() - millis < periodMillis;
        }
    }
    private Map<T, List<Entry>> map = Maps.newHashMap();

    public SlidingWindowMap(Collection<T> keys, long periodMillis, long maxEntries) {
        this.keys = keys;
        this.periodMillis = periodMillis;
        this.maxEntries = maxEntries;

        for (T key : keys) {
            List<Entry> l = Lists.newArrayList();
            map.put(key, l);
        }
    }

    private void doMaintenance() {
        // clean up expired entries
        for (T key : map.keySet()) {
            List<Entry> entries = map.get(key);
            for (Iterator<Entry> it = entries.iterator(); it.hasNext(); ) {
                Entry e = it.next();
                if (! e.isWithinPeriod()) {
                    it.remove();
                }
            }
        }
    }

    /**
     * @return the next allowed key (hasn't been less than maxEntries time withing
     * a period of periodMillis) or null if no such key is available.
     */
    public T getNextKey() {
        doMaintenance();

        List<T> keys = Lists.newArrayList(map.keySet());
        Collections.shuffle(keys);
        for (T key : keys) {
            List<Entry> entries = map.get(key);
            if (entries.isEmpty()
                    || (entries.size() < maxEntries && entries.get(0).isWithinPeriod())) {
                return newEntryAndReturnKey(key, entries);
            }
        }

        return null;
    }

    /**
     * @return the next key available if available. If not, sleep for one second and
     * try again.
     */
    public T getNextKeyOrBlock() {
        T result = getNextKey();
        while (result == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = getNextKey();
        }

        return result;
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private T newEntryAndReturnKey(T key, List<Entry> entries) {
        Entry newEntry = new Entry();
        newEntry.millis = now();
        entries.add(newEntry);
        return key;
    }
}
