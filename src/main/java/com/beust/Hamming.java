package com.beust;

public class Hamming {
  public static void main(String[] args) {
    int current = 1;
    int nTwo = 2;
    int nThree = 3;
    int nFive = 5;
    for (int i = 0; i < 30; i++) {
      int x= current * nTwo;
      int y = current * nThree; 
      int z = current * nFive;
      if (x < y && x < z) {
        current = x;
        nTwo = nTwo*2;
      } else if (y < x && y < z) {
        current = y;
        nThree = nThree*3;
      } else {
        current = z;
        nFive = nFive*5;
      }
      System.out.print(current + " ");
    }
  }
}
