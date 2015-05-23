package com.beust;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Exec {
    final static Logger logger = LoggerFactory.getLogger(Exec.class);

    private static final ScheduledExecutorService executor =
        Executors.newSingleThreadScheduledExecutor(
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread result = new Thread();
                    result.setName("BuggyExecutor");
                    return result;
                }
            });

    public static void main(String[] args) {
        executor.scheduleAtFixedRate(() -> logger.info("Tick"),
                0,
                1, TimeUnit.SECONDS);
    }
}
