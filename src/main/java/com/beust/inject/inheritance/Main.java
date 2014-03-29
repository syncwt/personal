package com.beust.inject.inheritance;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;


public class Main {

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new Module() {

      @Override
      public void configure(Binder binder) {
        binder.install(new FactoryModuleBuilder().build(A.IFactory.class));

      }
    });

    injector.getInstance(A.class).run();
  }
}
