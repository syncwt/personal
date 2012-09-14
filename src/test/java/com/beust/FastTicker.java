package com.beust;

import com.google.common.base.Ticker;

public class FastTicker extends Ticker {

  @Override
  public long read() {
    return System.nanoTime() * 10;
  }

}
