package com.beust.inject;

import com.beust.inject.MultipleBindings.D;
import com.beust.inject.MultipleBindings.DTest;
import com.google.inject.Binder;
import com.google.inject.Module;

public class TestModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(D.class).to(DTest.class);
    }

}
