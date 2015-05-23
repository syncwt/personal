package com.beust;

import java.util.Calendar;

class Elvis {
    private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static final Elvis INSTANCE = new Elvis();
    private final int beltSize;

    private Elvis() {
        this.beltSize = CURRENT_YEAR - 1930;
    }

    public int getBeltSize() {
        return this.beltSize;
    }

    public static void main(String[] args) {
        System.out.println("Elvis has a belt size of " + INSTANCE.getBeltSize());
    }
}