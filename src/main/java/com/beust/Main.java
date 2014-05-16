package com.beust;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
class Arg {
    @Parameter(names = "-long")
    public long l;
}

public class Main {
    @Inject
    private Arg arg;
    
    public static void main(String[] args) throws Exception {
        Injector inj = Guice.createInjector();
        inj.getInstance(Main.class).f();
        inj.getInstance(B.class).showInjection();
    }

    public void f() {
        new JCommander(arg).parse("-long", "32");
    }
}

class B {
    @Inject
    private Arg arg;

    public void showInjection() {
        System.out.println("Value: " + arg.l);
    }
}
