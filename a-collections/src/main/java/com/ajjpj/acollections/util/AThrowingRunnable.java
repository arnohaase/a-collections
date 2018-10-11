package com.ajjpj.acollections.util;


@FunctionalInterface
public interface AThrowingRunnable {
    void run() throws Throwable;

    default Runnable toRunnable() {
        return () -> AUnchecker.executeUnchecked(this);
    }

    static AThrowingRunnable fromRunnable(Runnable r) {
        return r::run;
    }
}
