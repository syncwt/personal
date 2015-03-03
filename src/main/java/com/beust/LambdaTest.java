package com.beust;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class LambdaTest {
    public static String[] strs = { "a", "aa", "aaa"};

    public static void main(String args[]) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        Set<Future<Integer>> set = new HashSet<Future<Integer>>();
        for (final String word : strs) {

//            Callable<Integer> c = (() -> word.length());

//            Future<Integer> future = pool.submit(c);
//            set.add(future);
        }
        int sum = 0;
        for (Future<Integer> future : set) {
            sum += future.get();
        }
        System.out.printf("The sum of lengths is %s%n", sum);
        System.exit(sum);
    }
}
