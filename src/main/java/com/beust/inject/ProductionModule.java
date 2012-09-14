package com.beust.inject;

import com.beust.inject.MultipleBindings.D;
import com.beust.inject.MultipleBindings.DProd;
import com.google.inject.Binder;
import com.google.inject.Module;

public class ProductionModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(D.class).to(DProd.class);
    }

}
