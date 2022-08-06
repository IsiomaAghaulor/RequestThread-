//WebServer.java: A Webserver class for managing multiple requests
package com.decagon;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


// WebServer is made a thread so to prevent blocking other client threads from running during testing.
public class WebServer extends Thread {
    ServerSocket socket;

    public WebServer(int port) throws IOException {
         socket = new ServerSocket(port);
    }

    public void run() {
        try {
            // Executor class for managing thread pools
            Executor executor = Executors.newCachedThreadPool();
            while (true)  {
                // Once a new connection is created, create a new request thread that handles the connection

                executor.execute(new RequestThread(socket.accept()));
                System.out.println("Client Connected!");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
