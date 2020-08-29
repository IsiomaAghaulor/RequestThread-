// Main.java: Driver class for WebServer Class
package com.decagon;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new WebServer(8100).start();

    }
}
