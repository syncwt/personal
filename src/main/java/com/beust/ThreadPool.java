package com.beust;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    private final ScheduledExecutorService EXECUTOR = new ScheduledThreadPoolExecutor(3) {
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            if (t == null && r instanceof Future<?>) {
                  try {
                    Future<?> future = (Future<?>) r;
                    if (future.isDone()) future.get();
                  } catch (CancellationException ce) {
                      t = ce;
                  } catch (ExecutionException ee) {
                      t = ee.getCause();
                  } catch (InterruptedException ie) {
                      Thread.currentThread().interrupt();
                  }
              }
             if (t != null) {
                 System.out.println("Error: " + t);
                 t.printStackTrace();
             }
           }
        };

    public static void main(String[] args) {
        new ThreadPool().run();
    }

    private void run() {
        EXECUTOR.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("Crash");
//                System.out.println("TICK");
            }
        },
        0,
        3, TimeUnit.SECONDS);
    }
}
