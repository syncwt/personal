package com.beust.inject;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

public class MultipleBindings {

    public static class D {
    }
    public static class DProd extends D {
    }
    public static class DTest extends D {
    }

    @Inject
    private D d;

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(Modules.override(new ProductionModule())
                .with(new TestModule()));
        inj.getInstance(MultipleBindings.class).run();
    }

    private void run() {
        System.out.println("Running with " + d);
    }
}
