package com.decagon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientMock implements Runnable {
    private int port;
    private String host;
    private Socket socket;
    private List<String> result;

    public ClientMock(int port, String host, List<String> result) {
        this.port = port;
        this.host = host;
        this.result = result;
    }

    public void run() {
        runClient();
    }

    public void runClient() {
        try {
            socket = new Socket(host, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream output = socket.getOutputStream();
            System.out.println(socket.isConnected());
            output.write("GET / HTTP/1.1\r\n".getBytes());
            output.write(String.format("Host: %s:%s\r\n", host, port).getBytes());
            output.flush();
            output.close();

            String line = input.readLine();
            while (line != null) {
                result.add(line);
                line = input.readLine();
            }


        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
