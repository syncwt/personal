package com.beust.rx;

import java.util.ArrayList;
import java.util.List;

import rx.Observable.Operator;
import rx.Subscriber;

public final class SlidingWindowOperator<T> implements Operator<List<T>, T> {
    private final int size;
    private List<T> buffer = new ArrayList<>();

    public SlidingWindowOperator(int size) {
        this.size = size;
    }

    @Override
    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        return new Subscriber<T>(child) {

            @Override
            public void onCompleted() {
                buffer = null;
                child.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                buffer = null;
                child.onError(e);
            }

            @Override
            public void onNext(T t) {
                buffer.add(t);
                if (buffer.size() == size) {
                    child.onNext(buffer);
                } else if (buffer.size() == size + 1) {
                    buffer = buffer.subList(1, buffer.size());
                    child.onNext(buffer);
                }
            }
        };
    }
}