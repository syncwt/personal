package com.beust.inject.inheritance;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;

public class A extends Base {
  public interface IFactory {
    A create(@Assisted("name") String name);
  }

  @Inject
  public A(String name, Injector inj) {
    super(name, inj);
  }

  public void run() {
    System.out.println("A: name:" + name + " dep1: " + dep1);
  }
}
