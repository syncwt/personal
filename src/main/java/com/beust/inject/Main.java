package com.beust.inject;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class Main {
  public static void main(String[] args) {
    Guice.createInjector(new Module() {

      @Override
      public void configure(Binder binder) {
        binder.install(new FactoryModuleBuilder()
          .build(BaseFactory.class));
      }
      
    }).getInstance(Main.class).run();
  }

//  @Inject
//  private A1 a1;

//  @Inject
//  private A2 a2;

  @Inject
  BaseFactory f;

  private void run() {
    System.out.println("Running a1: " + f.create1("a1"));// + " a2:" + f.create1("a2"));
  }
}
