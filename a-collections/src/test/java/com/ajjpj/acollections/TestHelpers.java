package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AUnchecker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


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
}
