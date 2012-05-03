package com.beust;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class GroupBy {

    static interface F<R, K> {
        R call(K k);
    }

    public static void main(String[] args) {
        F<Integer, String> f = new F<Integer, String>() {
            public Integer call(String s) {
                return s.length();
            }
        };
        List<String> l = ImmutableList.of("ab", "cde", "fgh", "ijkl", "mn", "opqrs");
        System.out.println(new GroupBy().groupBy(l, f));
    }

    private static <R, K> List<List<K>> groupBy(List<K> l, F<R, K> f) {
//        ListMultiMap<R, K> lmm = Multimaps.newListMultimap();
//        for (K k : k) {
//            
//        }
        return null;
    }
}
