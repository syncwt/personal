package com.beust;

import org.joda.time.DateTime;

public class Keep {
    private boolean keepGoing = true;
    public void setKeepGoing(boolean b) {
        keepGoing = b;
    }

    public boolean getKeepGoing() {
        return keepGoing;
    }

    static int x = 0;
    static Keep k = new Keep();

    static class Thread1 implements Runnable {
        @Override
        public void run() {
            System.out.println("Starting t1");
            while (k.getKeepGoing()) x++;
            System.out.println("t1 done");
        }
    }

    static class Thread2 implements Runnable {
        @Override
        public void run() {
            k.setKeepGoing(false);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new DateTime() + " STARTING");
        new Thread(new Thread1()).start();
        Thread.sleep(2000);
        new Thread(new Thread2()).start();
        System.out.println(new DateTime() + " DONE");
    }
}