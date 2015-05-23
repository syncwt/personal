package com.beust;

import java.util.function.Supplier;

public class Lambda {

    public void h() {
        
    }
    public static void f() {
        String x = "hello";
        Lambda l = new Lambda();
//        Function<?, ?> s = l::h;
    }

    private static void g(Supplier<String> s) {
    }

    public static void main(String[] args) {
        System.out.println(org.joda.time.format.DateTimeFormat.forPattern("MMddyyyy_hhmm")
                .print(new org.joda.time.DateTime()));
        Runnable r = () -> System.out.println("hello world");
        r.run();
    }
}
