package com.ajjpj.acollections.util;

import com.ajjpj.acollections.immutable.AVector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;


public class AUncheckerTest {
    @Test void testThrowUnchecked() {
        assertThrows(IOException.class, () -> AUnchecker.throwUnchecked(new IOException("yo")), "yo");
        assertThrows(SQLException.class, () -> AUnchecker.throwUnchecked(new SQLException("yeah")), "yeah");
    }

    @Test void testExecuteRunnable() {
        List<String> journal = new ArrayList<>();

        AThrowingRunnable r = () -> {
            journal.add("i was here");
            throw new IOException("yo");
        };

        assertThrows(IOException.class, () -> AUnchecker.executeUnchecked(r), "yo");
        assertEquals(AVector.of("i was here"), journal);
    }

    @Test void testExecuteCallable() {
        assertEquals("a", AUnchecker.executeUnchecked(() -> "a"));

        List<String> journal = new ArrayList<>();

        Callable<Object> r = () -> {
            journal.add("i was here");
            throw new IOException("yo");
        };

        assertThrows(IOException.class, () -> AUnchecker.executeUnchecked(r), "yo");
        assertEquals(AVector.of("i was here"), journal);
    }
}
