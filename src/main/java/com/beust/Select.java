package com.beust;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Select {

  private static final int N = 5;
  private static final int RUNS = 1000000;

  public static void main(String[] args) {
//    for (int i = 0; i < 10; i++) System.out.println(new Random().nextInt(10));
    verify();
  }

  private static int[][] createMatrix() {
    int[][] result = new int[N][];
    for (int i = 0; i < N; i++) {
      result[i] = new int[N];
      for (int j = 0; j < N; j++) {
        result[i][j] = 0;
      }
    }
    return result;
  }

  private static void verify() {
//    for (int i = 0; i < N; i++) {
//      mat[i] = new int[N];
//      for (int j = 0; j < N; j++) {
//        mat[i][j] = 0;
//      }
//    }

    int[][] mat = createMatrix();
    double[] averageMat = new double[N];
    double[] stdev = new double[N];

    Map<Long, Integer> counts = Maps.newHashMap();
    for (int i = 0; i < RUNS; i++) {
      List<Integer> array = newArray();
      List<Integer> numbers =
          select(array);
//          array;
//      Collections.shuffle(numbers);

      // Count matrix
      for (int j = 0; j < N; j++) {
        mat[j][numbers.get(j)]++;
      }

      // Average matrix
      for (int k = 0; k < N; k++) {
        averageMat[k] += numbers.get(k);
        int var = Math.abs(2 - numbers.get(k));
        stdev[k] += var*var;
      }
    }
    for (int i = 0; i < N; i++) {
      averageMat[i] /= RUNS;
      stdev[i] = Math.sqrt(stdev[i] / RUNS);
    }
    System.out.println("Averages:" + Arrays.toString(averageMat));
    System.out.println("Stdev:" + Arrays.toString(stdev));

//    System.out.println(counts);

    int failures = 0;
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        System.out.print(String.format("%7s", " " + mat[i][j]));
        float delta = Math.abs((RUNS / N) - mat[i][j]);
        float percent = delta * 100f / N / RUNS;
        if (percent > 0.01f) {
          failures++;
        }
      }
      System.out.println("");
    }

    System.out.println("Runs: " + RUNS + ", failures: " + failures + "/" + (N*N));
  }

  private static List<Integer> select(List<Integer> result) {
    int index = randomV1(N);
//    int index = new Random().nextInt(N);
    for (int i = N; i > 1; i--) {
      Collections.swap(result, i-1, rnd.nextInt(i - 1));
    }
//    for (int i = 0; i < N; i++) {
//      Collections.swap(result, i, new Random().nextInt(N));
//    }
    return result;
  }

  private static List<Integer> newArray() {
    List<Integer> array = Lists.newArrayList();
    for (int i = 0; i < N; i++) {
      array.add(i);
    }
    return array;
  }

  private static int randomV1(int i) {
    return new Random().nextInt(i);
  }

  private static Random rnd = new Random();
  private static int randomV2(int i) {
    return rnd.nextInt(i);
  }

  private static int randomV3(int i) {
    double r = Math.random() * i;
    int result = (int) Math.floor(r);
    return result;
  }
}
