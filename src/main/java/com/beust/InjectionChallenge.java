package com.beust;

import com.google.inject.Inject;
import com.google.inject.Injector;

class Config {
    int port = 1024;
}

public class InjectionChallenge{
    @Inject
    private BBB b;

    public void a() {
//        b.b();
    }

    public static void main(String[] args) {
        Injector inj = null; //
        inj.getInstance(InjectionChallenge.class).a();
    }
}

class BBB {
    @Inject
    private C c;

    public void b() {
        c.c();
    }
}

class C {
    @Inject
    private Config config;

    public void c() {
        System.out.println("Port is " + config.port);
    }
}
