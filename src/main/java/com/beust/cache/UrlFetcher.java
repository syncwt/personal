package com.beust.cache;

import java.util.concurrent.Callable;

public class UrlFetcher implements Callable<String> {

  private String url;
  private PendingCache cache;

  public UrlFetcher(String url, PendingCache cache) {
    this.url = url;
    this.cache = cache;
  }

  @Override
  public String call() throws Exception {
    Callable<String> callable = new Callable<String>() {
      @Override
      public String call() throws Exception {
        String result = new MyUrl(url).getContent();
        return result;
      }
    };

    return cache.get(url, callable);
  }

  private static void p(String s) {
    System.out.println(Thread.currentThread().getId() + " [UrlFetcher] " + s);
  }
}
