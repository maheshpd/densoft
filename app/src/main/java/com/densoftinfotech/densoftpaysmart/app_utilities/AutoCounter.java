package com.densoftinfotech.densoftpaysmart.app_utilities;

import java.util.concurrent.atomic.AtomicInteger;

public class AutoCounter {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final AtomicInteger counter1 = new AtomicInteger(0);

    public AutoCounter() {
    }

    public static int getCounterPlusOne() {
        if (counter1.get() != 0) {
            counter1.getAndIncrement();
        }
        return counter.getAndIncrement();
    }

    public static int getPlusOneCounter() {
        if (counter1.get() != 0) {
            counter1.incrementAndGet();
        }
        return counter.incrementAndGet();
    }

    /*public static int getCounterCurrentVal() {
        return counter1.get();
    }*/
}
