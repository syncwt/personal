package com.beust.rx;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class Rx {
    final static Logger log = LoggerFactory.getLogger(Rx.class);

    public static void main(String[] args) throws InterruptedException {
        Rx a = new Rx();
        a.run();
    }

    public void run() throws InterruptedException {
        Observable<String> s2 = Observable.interval(300, TimeUnit.MILLISECONDS)
              .map(new Func1<Long, String>() {
                  @Override
                  public String call(Long t1) {
                      return "ms #" + t1;
                  }
          });
        Observable<String> s1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long t1) {
                        return "sec #" + t1;
                    }
            });
                ;

        Observable.combineLatest(s1, s2, new Func2<String, String, String> () {
                @Override
                public String call(String v1, String v2) {
                    return "Combined: " + v1 + " " + v2;
                }
            })
            .take(10)
            .subscribe(new Action1<String>() {
                @Override
                public void call(String t1) {
                    log.info("Received " + t1);
                }
        });

        Thread.sleep(10000);
    }
}
