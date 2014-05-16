package com.beust;

import java.util.Map;

import com.google.common.collect.Maps;

public class SumZero {
    private static final int[] NUMBERS = new int[] { -10, -8, -6, 0, 3, 5, 18 };

    private Pair<Integer, Integer> indicesThatAddUpTo(int indexStart, int target) {
      // number, index
     Map<Integer, Integer> map = Maps.newHashMap();
       for (int i = indexStart; i < NUMBERS.length; i++) {
         int n = NUMBERS[i];
          if (map.containsKey(target - n)) {
            return Pair.of(i, map.get(target - n));
          } else {
            map.put(n, i);
          }
      }
      return null;
    }

    private void run() {
      for (int i = 0; i < NUMBERS.length; i++) {
        Pair<Integer, Integer> pair = indicesThatAddUpTo(i, -NUMBERS[i]);
        if (pair != null) {
            System.out.println("Found: " + i + ", " + pair.first()
                    + ", " + pair.second());
        }
      }
    }

    public static void main(String[] args) {
        // System.out.println(new SumZero().indicesThatAddUpTo(0, 5));
        new SumZero().run();
    }
}
