package com.beust.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class Timer {

    public static void main(String[] args) throws InterruptedException {
        Timer a = new Timer();
        a.run();
        Thread.sleep(10000);
    }

    private boolean display = true;

    private void run() {
        Observable.just("foo")
            .delay(2, TimeUnit.SECONDS)
            .filter((String s) -> display)
            .subscribe((String s) -> System.out.println("String " + s));
    }
}
