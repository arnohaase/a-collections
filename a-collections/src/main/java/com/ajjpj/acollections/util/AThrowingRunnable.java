package com.ajjpj.acollections.util;


@FunctionalInterface
public interface AThrowingRunnable {
    void run() throws Throwable;

    default Runnable toRunnable() {
        return () -> AUnchecker.executeUncheckedVoid(this);
    }

    static AThrowingRunnable fromRunnable(Runnable r) {
        return r::run;
    }
}
