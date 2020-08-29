package com.decagon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.lang.reflect.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RequestThreadTest {
    ClientMock clientMock;
    RequestThread requestThread;

    @BeforeEach
    void setup () throws IOException {
        requestThread = new RequestThread(new Socket("localhost", 8080));
    }

    @Test
    void testingIsJson() throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Method privateIsJson = RequestThread.class.getDeclaredMethod("isJson", String.class);
        privateIsJson.setAccessible(true);
        Boolean result = (Boolean) privateIsJson.invoke(requestThread, "/json");
        assertTrue(result);
        result = (Boolean) privateIsJson.invoke(requestThread, "/");
        assertFalse(result);

    }

    @Test
    void testingIsResourceFound() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateIsResourceFound = RequestThread.class.getDeclaredMethod("isResourceFound", String.class);
        privateIsResourceFound.setAccessible(true);
        Boolean result = (Boolean) privateIsResourceFound.invoke(requestThread, "/");
        assertTrue(result);
        result = (Boolean) privateIsResourceFound.invoke(requestThread, "/notfound");
        assertFalse(result);
    }

    void testingPathIsSetCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateSetPath = RequestThread.class.getDeclaredMethod("setPath", String.class);
        privateSetPath.setAccessible(true);
        Path result = (Path) privateSetPath.invoke(requestThread, "/");
        assertTrue(Files.exists(result));
        result = (Path) privateSetPath.invoke(requestThread, "/not");
        assertFalse(Files.exists(result));
    }

}