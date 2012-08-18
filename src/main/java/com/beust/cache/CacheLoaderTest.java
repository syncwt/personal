package com.beust.cache;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CacheLoaderTest {
  private void run() throws ExecutionException, InterruptedException {
    PendingCache wc = new PendingCache();
    String url = "http://twitter.com/cbeust";
    ExecutorService executor = Executors.newFixedThreadPool(5);
    CompletionService<String> ecs = new ExecutorCompletionService<String>(executor);
    int n = 20;
    for (int i = 0; i < n; i++) {
      UrlFetcher uf = new UrlFetcher(url, wc);
      ecs.submit(uf);
    }
    List<String> results = Lists.newArrayList();
    for (int i = 0; i < n; i++) {
      Future<String> t = ecs.take();
      results.add(t.get());
    }
    p("Results: " + results.size());
    executor.shutdown();
  }

  private void p(String string) {
    System.out.println(Thread.currentThread().getId() + " [CacheLoaderTets] " + string);
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    new CacheLoaderTest().run();
  }
}
