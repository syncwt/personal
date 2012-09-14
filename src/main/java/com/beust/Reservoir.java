package com.beust;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class Reservoir {

    public Reservoir(List<Iterator<Integer>> l, int n) {
        List<Integer> result = Lists.newArrayList();
        int i = 0;
        Iterator<Integer> it = l.get(i);
        while (i < n && it.hasNext()) {
            result.add(it.next());
        }
    }

    public static void main(String[] args) {
        int index = 0;
        List<Iterator<Integer>> l = Lists.newArrayList();
        for (int i = 0; i < 50; i++) {
            List<Integer> l2 = Lists.newArrayList();
            int r = (int) (Math.random() * 50);
            for (int j = 0; j < r; j++) {
                l2.add(index++);
            }
            l.add(l2.iterator());
        }
        System.out.println("Lists:"+ index + " " + l);
        new Reservoir(l, 20);
    }
}
