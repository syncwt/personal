package com.beust;

import java.util.HashMap;
import java.util.Map;

import com.beust.jcommander.JCommander;


public class A {
    final int a;
    public A(int a) {
        this.a = a;
        JCommander j;
    }
    @Override
    public String toString() {
        return "A: " + a;
    }

    public static void main(String[] args) {
        Map<Integer, String> m = new HashMap<>();
        m.put(1000, "a");
        String s = m.get(1000);
        System.out.println("s: " + s);
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
