package com.beust;

import java.util.List;

import com.google.common.collect.Lists;

public class NextPermutation {

  /**
   * 132 -> 213
   * 38276 -> (38 276) -> 276 -> 672 -> 38672
   * 375 -> 573
   */
  public static void main(String[] args) {
  System.out.println(new NextPermutation().next(38276));
//      System.out.println(new NextPermutation().next(38276));
  }

  private List<Integer> next(int n) {
    System.err.println("Called with " + n);
    List<Integer> l = toArray(n);
    if (l.size() == 1) return l;

    // Find the first digit that's larger than the last one
    Integer lastIndex = l.size() - 1;
    Integer last = l.get(lastIndex);
    List<Integer> result = Lists.newArrayList();
    for (int i = lastIndex; i >= 0; i--) {
      int current = l.get(i);
      if (current < last) {
        swap(l, i, lastIndex);
        result.addAll(l.subList(0, i));
        result.addAll(next(toInteger(l.subList(current, lastIndex + 1))));
        System.out.println("Returning " + result);
        return result;
//        l.set(i, last);
//        l.set(lastIndex, current);
//        System.out.println(l);
      }
    }
    return result;
  }

  private void swap(List<Integer> l, int from, int to) {
    int tmp = l.get(from);
    l.set(from, l.get(to));
    l.set(to, tmp);
  }

  private int toInteger(List<Integer> l) {
    int result = 0;
    for (Integer n : l) {
      result = result * 10 + n;
    }
    return result;
  }

  private List<Integer> toArray(int n) {
    List<Integer> result = Lists.newArrayList();
    while (n > 0) {
      result.add(0, n % 10);
      n /= 10;
    }
    return result;
  }
}
