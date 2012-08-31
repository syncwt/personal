package com.beust.inject;

import com.google.common.base.Objects;

public class Base {

  private Resource resource;
  private String name;

  public Base(Resource resource, String name) {
    this.resource = resource;
    this.name = name;
  }

  public void run() {
    System.out.println("Running " + getClass());
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("class", getClass())
        .add("name", name)
        .toString();
  }
}
