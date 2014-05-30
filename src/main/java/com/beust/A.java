package com.beust;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;


public class A {
    final int a;
    public A(int a) {
        this.a = a;
    }
    @Override
    public String toString() {
        return "A: " + a;
    }

    public static void main(String[] args) {
        @Nonnull
        ImmutableList<Integer> l = ImmutableList.of(3, 5, 7, 1, 10, 9);
        
    }
}

class BB extends A {
    int value;
    public BB(int a, int value) {
        super(a);
        this.value = value;
    }

    @Override
    public String toString() {
        value = 3;
        return super.toString();
    }

    public static void foo(A a) {
        System.out.println(a);
    }
}
