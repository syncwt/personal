package com.beust.inject;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class A1 extends Base {

  @Inject
  public A1(Resource resource, @Assisted String name) {
    super(resource, name);
  }
}
