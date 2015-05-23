package com.beust;

import java.util.BitSet;
import java.util.function.Predicate;

public class Bit {
    public static void main2(String[] args) {
        BitSet bs = new BitSet();
        int n = 127;
        int i = 0;
        while (n > 0) {
            if (n % 2 == 1) {
                bs.set(i);
            }
            i++;
            n /= 2;
        }
        System.out.println("Bitset: " + bs);
    }

    static boolean predicate(Predicate<?> p) {
        return true;
    }

    public static void main(String[] args) {
        Predicate<String> p = x -> true;
        System.out.println(predicate(p));
    }

}
