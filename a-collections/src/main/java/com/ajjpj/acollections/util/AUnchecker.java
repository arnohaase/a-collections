package com.ajjpj.acollections.util;


import java.util.concurrent.Callable;

/**
 * This class throws an arbitrary exception without requiring it to be declared in a throws clause. The exceptions remain "as is", they
 *  are not modified or wrapped in any way and can be caught by callers. The only difference is that essentially any exception can become
 *  "unchecked".
 *
 * <p> The concept of "checked exceptions" that must be declared in a method's 'throws' clause is part of the Java language, but it is
 *      not enforced by the JVM at runtime. AUnchecker uses a soft spot in Java's checks to allow throwing checked exceptions without
 *      declaring them.
 *
 * <p> That can be useful if code in a callback can throw checked exceptions:
 *
 * <p> {@code AList<File> files = ...;}
 * <p> {@code // This does not compile: AList<File> normalized = files.map(f -> f.getCanonicalFile());}
 * <p> {@code AList<File> normalized = files.map(f -> AUnchecker.executeUnchecked(() -> f.getCanonicalFile()));}
 */
public class AUnchecker {
    /**
     * Throws an arbitrary {@link Throwable} in a way that does not force surrounding code to declare it.
     *
     * @param th the Throwable to throw
     */
    public static void throwUnchecked(Throwable th) {
        AUnchecker.<RuntimeException> doIt(th);
    }

    private static <T extends Throwable> void doIt(Throwable th) throws T {
        //noinspection unchecked
        throw (T) th;
    }

    /**
     * Executes a callback, throwing all exceptions that may occur without requiring them to be declared in a 'throws' clause.
     *
     * @param callback the callback to execute
     */
    public static void executeUnchecked(AThrowingRunnable callback) {
        try {
            callback.run();
        }
        catch (Throwable exc) {
            throwUnchecked (exc);
        }
    }

    /**
     * Executes a callback, throwing all exceptions that may occur without requiring them to be declared in a 'throws' clause.
     *
     * @param callback the callback to execute
     * @param <R>      the callback's result type
     * @return the callback's result
     */
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
