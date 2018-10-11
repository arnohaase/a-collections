package com.ajjpj.acollections.util;


import java.util.Objects;

public class APair<T1, T2> {
    public final T1 _1;
    public final T2 _2;

    public APair (T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    @Override public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        APair<?, ?> aPair = (APair<?, ?>) o;
        return Objects.equals(_1, aPair._1) &&
                Objects.equals(_2, aPair._2);
    }

    @Override public int hashCode () {
        return Objects.hash(_1, _2);
    }

    @Override public String toString () {
        return "APair{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                '}';
    }
}
