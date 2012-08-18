package com.beust.cache;

import com.google.common.io.Resources;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public class UrlFetcher implements Callable<String> {

  private String url;
  private PendingCache<String, String> cache;

  public UrlFetcher(String url, PendingCache<String, String> cache) {
    this.url = url;
    this.cache = cache;
  }

  @Override
  public String call() throws Exception {
    Callable<String> callable = new Callable<String>() {
      @Override
      public String call() throws Exception {
        p("Fetching: " + url + " (should only happen once per url)");
        return Resources.toString(new URL(url), Charset.defaultCharset());
      }
    };

    return cache.get(url, callable);
  }

  private static void p(String s) {
    System.out.println(Thread.currentThread().getId() + " [UrlFetcher] " + s);
  }
}
