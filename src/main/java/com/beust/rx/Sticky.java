package com.beust.rx;

import rx.subjects.PublishSubject;


public class Sticky {
    public static void main(String[] args) {
        Sticky a = new Sticky();
        a.run();
    }

    public void run() {
        PublishSubject<String> s = PublishSubject.create();
    }


}
