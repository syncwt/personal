package com.beust;

import java.util.HashMap;
import java.util.Map;

public class Pythagorean {

  static class AAA {}
  static class BBB extends AAA {}
 
  public static void f() {
    Map<Integer, AAA> m = new HashMap();
    m.put(3, new BBB());
  }

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 333; i++) {
      for (int j = i; j < (1000 + i)/2; j++) {
        int z = 1000 - i - j;
        if (z*z == i*i + j*j) {
          System.out.println("Found: " + i + " " + j + " " + z
              + " time:" + (System.currentTimeMillis() - start));
        }
      }
    }
  }
}
