package com.beust.inject;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

public class Inheritance {

    @Singleton
    static class Config {
    }

    static class A {
        @Inject
        private Config config;
        private String name;
        public A(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return "A: " + name + " config:" + config;
        }
    }

    static class B extends A {
        public B() {
            super("BBB");
        }
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector();
        B b = inj.getInstance(B.class);
        System.out.println(b);
    }
}
