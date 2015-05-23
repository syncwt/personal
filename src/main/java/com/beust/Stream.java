package com.beust;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

public class Stream {

    static boolean isOdd(int n) { return (n % 2) == 1; }

    public void f() {
        List<Integer> l = ImmutableList.of(1,2,3,4,5);
        List<Integer> result = l.stream()
                .filter(Stream::isOdd)
                .filter(n -> n >= 5)
                .collect(Collectors.toList());
        System.out.println(result);
    }

    public static void main(String[] args) {
        new Stream().f();
    }
}
