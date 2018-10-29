package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AUnchecker;

import java.io.*;
import java.util.function.BiPredicate;
import java.util.function.Function;


public class TestHelpers {
    public static <T> T serDeser (T o) {
        return AUnchecker.executeUnchecked(() -> {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
            final byte[] bytes = baos.toByteArray();

            final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            final ObjectInputStream ois = new ObjectInputStream(bais);
            //noinspection unchecked
            return (T) ois.readObject();
        });
    }

    public static class SerializableDoublingFunction implements Function<Integer,Integer>, Serializable {
        @Override public Integer apply (Integer i) {
            return 2*i;
        }
    }
}
