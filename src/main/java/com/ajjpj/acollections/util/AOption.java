package com.ajjpj.acollections.util;


public class AOption<T> {
    //TODO get
    //TODO map etc.

    public static <T> AOption<T> some(T o) {
        return new ASome<>(o);
    }
    public static <T> AOption<T> none() {
        //noinspection unchecked
        return ANone;
    }

    private static class ASome<T> extends AOption<T> {
        private final T el;

        ASome (T el) {
            this.el = el;
        }
    }

    private static final AOption ANone = new AOption() {
    };

}
