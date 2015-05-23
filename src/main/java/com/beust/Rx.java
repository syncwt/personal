package com.beust;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.observables.BlockingObservable;
import rx.schedulers.Schedulers;

public class Rx {

    public static void main(String[] args) throws InterruptedException {
        new Rx().run();
        Thread.sleep(200);
    }

    public static class SlidingWindowOperator<R, T> implements Operator<R, T> {

        @Override
        public Subscriber<? super T> call(Subscriber<? super R> t1) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private void log(String s) {
        System.out.println("{Thread: " + Thread.currentThread().getName() + "} " + s);
    }

    private static void p(String s) {
        System.out.println(Thread.currentThread().getName() + " " + s);
    }

    private void run() {
        Observable
            .range(1, 3)
            .subscribeOn(Schedulers.computation())
            .flatMap(n -> {
                p("Subscribe first");
                return Observable.just(n);
            })
//            .filter(n -> {
//                return true;
//            })
            .subscribeOn(Schedulers.io())
            .flatMap(n -> {
                p("Subscribe second");
                return Observable.just(n);
            })
            .observeOn(Schedulers.io())
            .subscribe(integer ->
                p("    Observe " + integer)
            );
    }

    private void run6() throws InterruptedException {
        Observable
        .interval(10, TimeUnit.MILLISECONDS)
        .take(5)
        .subscribeOn(Schedulers.io())
        .flatMap(input -> {
            System.out.println(Thread.currentThread().getName()
                    + " flatMap");
            return Observable.just(input * 2);
        })
//        .toBlocking()
        .observeOn(Schedulers.newThread())
        .subscribe(s -> {
            System.out.println("   Observing " + Thread.currentThread().getName()
                    + ": " + s);            
        });
        Thread.sleep(2000);
    }

    private void run5() {
     // This does not block.
        BlockingObservable<Long> observable = Observable
            .interval(1, TimeUnit.SECONDS)
            .toBlocking();

        // This blocks and is called for every emitted item.
        observable.forEach(counter -> System.out.println("Got: " + counter));
    }

    private void run4() {
        Observable.range(0, 3)
            .subscribeOn(Schedulers.computation())
            .map(n -> {
                log("Mapping " + n);
                return n * 2;
            })
            .observeOn(Schedulers.io())
            .subscribe(n -> log("Observing value: "  + n));
    }

    private void run3() {
        Observable.interval(1, TimeUnit.MILLISECONDS).take(5).buffer(3, 1)
                .subscribe(System.out::println);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void run2() {
        Observable<String> o = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> t1) {
                int i = 0;
                while (i < 10) {
                    t1.onNext("Message " + i);
                    i++;
                }
            }
        });
        o.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String t) {
                System.out.println("Received " + t);
            }
            
        });
//        Observable<String> o = Observable.empty();
//        ReplaySubject rs = ReplaySubject.createWithSize(1);
//        rs.subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                System.out.println("Subscriber 1 received " + s);
//            }
//        });
//        rs.onNext("Message 1");
//        rs.onNext("Message 2");
//        rs.onNext("Message 3");
//        rs.onNext("Message 4");
//        rs.subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                System.out.println("Subscriber 2 received " + s);
//            }
//        });
    }
}
