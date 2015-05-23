package com.beust;

import java.util.HashMap;
import java.util.Map;

public class Finality {

    // final // Uncomment final to make this program safe.
    Map<String, Integer> map;

    Finality() {
        this.map = populate();
    }

    public static final void main(String... args) throws InterruptedException {
        final Finality[] racy = new Finality[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                racy[0] = new Finality();
            }
        }).start();

        Finality f;
        while ((f = racy[0]) == null) {
            Thread.sleep(100L);
        }
        System.out.println(f.map.size());
    }

    private static Map<String, Integer> populate() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("one", 1);
        map.put("two", 2);
        return map;
    }
}