package com.beust;

import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.beust.inject.MultipleBindings.D;
import com.beust.inject.ProductionModule;
import com.google.inject.Inject;

@Guice(modules = ProductionModule.class)
public class MultipleBindingsTest {

    @Inject
    private D d;

    @Test
    public void f() {
        System.out.println("Testing with " + d);
    }
}
