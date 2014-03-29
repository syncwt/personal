package com.beust.inject.inheritance;

import com.google.inject.Inject;
import com.google.inject.Injector;


public class Base {
  @Inject
  protected Dep1 dep1;
  protected String name;

  protected Base(String name, Injector inj) {
    this.name = name;
//    inj.injectMembers(this);
//    this.dep1 = inj.getInstance(Dep1.class);
  }
}

