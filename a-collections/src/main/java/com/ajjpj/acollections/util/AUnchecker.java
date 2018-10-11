package com.ajjpj.acollections.util;


import java.util.concurrent.Callable;

/**
 * This class throws an arbitrary exception without requiring it to be declared in a throws clause

 */
public class AUnchecker {
    public static void throwUnchecked(Throwable th) {
        AUnchecker.<RuntimeException> doIt(th);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void doIt(Throwable th) throws T {
        throw (T) th;
    }

    @SuppressWarnings("unused")
    public static void executeUnchecked(AThrowingRunnable callback) {
        try {
            callback.run();
        }
        catch (Throwable exc) {
            throwUnchecked (exc);
        }
    }

    public static <R> R executeUnchecked(Callable<R> callback) {
        try {
            return callback.call();
        }
        catch (Throwable exc) {
            throwUnchecked (exc);
            return null; //for the compiler
        }
    }
}
