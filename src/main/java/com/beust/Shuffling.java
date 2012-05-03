package com.beust;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public class Shuffling {
  private static final int N = 10;
  private static final int RUNS = 10000000;

  public static boolean isOk(int count) {
    float delta = Math.abs((RUNS / N) - count);
    float percent = delta * 100f / N / RUNS;
    return percent < 0.01f;
  }

  public static void measure(int[][] mat) {
    int failures = 0;
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (! isOk(mat[i][j])) {
          failures++;
        }
      }
    }
    System.out.println(failures + " failures");
  }

  public static List<Integer> shuffle(Random rnd) {
    List<Integer> list = Lists.newArrayList();
    for (int i = 0; i < N; i++) {
      list.add(i);
    }
    int size = list.size();
    for (int i=size; i>1; i--) {
        Collections.swap(list, i-1, rnd.nextInt(i));
    }
    return list;
  }

  public static List<Integer> bitShuffle(Random rnd) {
    List<Integer> list = Lists.newArrayList();
    List<Boolean> bits = Lists.newArrayList();
    for (int i = 0; i < N; i++) {
      bits.add(false);
    }
    for (int i = 0; i < N; i++) {
      int initialR = rnd.nextInt(N - i);
      int r = initialR;
      while (r < N && bits.get(r)) r++;
      bits.set(r, true);
      list.add(r);
    }
    return list;
  }

  private static int[][] mat = new int[N][];

  public static void main(String[] args) {
    for (int i = 0; i < N; i++) {
      mat[i] = new int[N];
    }

    List<Integer> l = Lists.newArrayList();
    for (int i = 0; i < RUNS; i++) {
      l = bitShuffle(new Random());
      for (int j = 0; j < l.size(); j++) {
        mat[j][l.get(j)]++;
      }
    }

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (isOk(mat[i][j])) {
          System.out.format("%10s ", "Ok");
        } else {
          System.out.format("%10s ", mat[i][j]);
        }
      }
      System.out.println("");
    }
    measure(mat);
  }
}
