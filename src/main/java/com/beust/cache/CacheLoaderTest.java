package com.beust.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CacheLoaderTest {
  private void runRegularCache() throws InterruptedException, ExecutionException {
    final LoadingCache<String, String> rc = CacheBuilder.newBuilder()
        .recordStats()
        .build(
            new CacheLoader<String, String>() {
              @Override
              public String load(String url) throws Exception {
                p("Fetching: " + url + " (should only happen once per url)");
                return Resources.toString(new URL(url), Charset.defaultCharset());
              }
            });
    runCaches(rc);
    CacheStats stats = rc.stats();
    p("Cache stats:" + stats);
  }

  private void runCaches(final LoadingCache<String, String> rc)
      throws InterruptedException, ExecutionException {
    List<String> urls = ImmutableList.of("http://twitter.com/cbeust", "http://google.com");
    ExecutorService executor = Executors.newFixedThreadPool(3);
    CompletionService<String> ecs = new ExecutorCompletionService<String>(executor);
    int workerCount = 5;
    for (final String url : urls) {
      for (int i = 0; i < workerCount; i++) {
        Callable<String> callable = new Callable<String>() {
          @Override
          public String call() throws Exception {
            p("Getting value from cache " + url);
            return rc.get(url);
          }
        };
        ecs.submit(callable);
      }
    }
    List<String> results = Lists.newArrayList();
    for (int i = 0; i < workerCount * urls.size(); i++) {
      Future<String> t = ecs.take();
      results.add(t.get());
    }
    p("Results: " + results.size());
    executor.shutdown();
  }

  private void p(String string) {
    System.out.println(Thread.currentThread().getId() + " [CacheLoaderTest] " + string);
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    new CacheLoaderTest().runRegularCache();
  }
}
