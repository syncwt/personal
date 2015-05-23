package com.beust.rx;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class Ticker  {
    static class Tick{
        BigDecimal getAsk() {
            return new BigDecimal(10.0);
        }
        BigDecimal getBid() {
            return new BigDecimal(8.0);
        }
    }

    static class ExchangeService {
        Observable<Tick> getTick() {
            return Observable.just(new Tick());
        }
    }

    private static ExchangeService exchangeService = new ExchangeService();

    public static void main(String[] args) throws InterruptedException {
        difference().subscribe((BigDecimal t1) -> System.out.println("Difference: " + t1));
        Thread.sleep(10000);
    }
 
    public static Observable<Tick> ticker() {
        return exchangeService
                .getTick()
                .delay(1000L, TimeUnit.MILLISECONDS)
                .repeat()
                .asObservable();
    }
 
    public static Observable<BigDecimal> ask() {
        return ticker()
                .map(t -> t.getAsk())
                .asObservable();
    }
 
    public static Observable<BigDecimal> bid() {
        return ticker()
                .map(t -> t.getBid())
                .asObservable();
    }
 
    public static Observable<BigDecimal> difference() {
        Observable<BigDecimal> result = Observable
                .zip(bid(), ask(), (bid, ask) -> ask.subtract(bid));
        return result;
 
    }
}