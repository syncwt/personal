package com.beust.inject;

import com.google.inject.TypeLiteral;

public interface BaseFactory {
  <T> T create1(TypeLiteral<T> type, String name);
//  A2 create2(String name);
}
