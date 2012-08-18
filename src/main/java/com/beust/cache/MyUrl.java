package com.beust.cache;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

class MyUrl {

  private String content;

  public MyUrl(String url) throws MalformedURLException, IOException {
    p("Fetching: " + url);
    this.content = Resources.toString(new URL(url), Charset.defaultCharset());
    p("... done!");
  }

  private static void p(String string) {
    System.out.println(Thread.currentThread().getId() + " [MyUrl] " + string);
  }

  public String getContent() {
    return content.substring(0, 100);
  }
}
