// RequestThread.java:


package com.decagon;


import java.io.*;
import java.net.Socket;
import java.nio.file.*;

public class RequestThread implements Runnable {
    Socket request;

    public RequestThread(Socket request) {
        this.request = request;

        // GET /jso HTTP/1.1
    }

    @Override
    public void run() {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            OutputStream writer = request.getOutputStream();
            String line = reader.readLine();

            // get the resource been sought by the client
            String resource = getResource(line);
            boolean foundResource = isResourceFound(resource);
            Path path = setPath(resource);

            // set the contentType of the path, to be returned.
            String contentType = Files.probeContentType(path);

            // handles writing to the clients output stream
           writeDataToOutput(path, writer, contentType, foundResource);

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            ioException.printStackTrace();
        }
    }

    /**
     * Writing data to the clients output stream is delegated to writeDataToOutput method
     * After writing, it closes the output stream;
     * @param path
     * @param writer
     * @param contentType
     * @param foundResource
     * @throws IOException
     */
    private void writeDataToOutput(Path path, OutputStream writer, String contentType, boolean foundResource) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(path.toFile()));
        writeHeadersToOutput(writer, contentType, foundResource);
        while (true) {
            String line = fileReader.readLine();
            if (line == null) break;
            writer.write(line.getBytes());
        }
        writer.write("\r\n\r\n".getBytes());
        writer.flush();
        writer.close();
    }

    /**
     * getResource get the resource being sought after
     * @param line
     * @return
     */
    private String getResource(String line) {
        String resourceRequested = line.split(" ")[1];
        System.out.println(resourceRequested);
        return resourceRequested;
    }

    /**
     * Checks if the resource is a json file;
     * @param resource
     * @return
     */
    private boolean isJson (String resource) {
        return resource.equals("/json");
    }

    /**
     * setPath sets the path to the resource being sought
     * sets the path to notfound.html, if resource is not found;
     * @param resource
     * @return
     */
    private Path setPath (String resource) {
        if (isJson(resource)) return Paths.get("src/main/resources/response.json");
        else if(resource.equals("/PodA")) return Paths.get("src/main/resources/PodA.html");
        else if(resource.equals("/PodB")) return Paths.get("src/main/resources/PodB.html");
        else if(resource.equals("/PodC")) return Paths.get("src/main/resources/PodC.html");
        else if(resource.equals("/PodD")) return Paths.get("src/main/resources/PodD.html");

        else if(resource.equals("/PodA/json")) return Paths.get("src/main/resources/PodA.json");
        else if(resource.equals("/PodB/json")) return Paths.get("src/main/resources/PodB.json");
        else if(resource.equals("/PodC/json")) return Paths.get("src/main/resources/PodC.json");
        else if(resource.equals("/PodD/json")) return Paths.get("src/main/resources/PodD.json");
        else if (!resource.equals("/")) return Paths.get("src/main/resources/notfound.html");
        return Paths.get("src/main/resources/index.html");
    }

    /**
     * isResourceFound returns true/false depending on
     * if the resource was found
     * @param resource
     * @return
     */
    private boolean isResourceFound(String resource) {
        return resource.equals("/") || resource.equals("/json") ||  resource.equals("/PodA")
                || resource.equals("/PodB") || resource.equals("/PodC") || resource.equals("/PodD")
                || resource.equals("/PodA/json") || resource.equals("/PodB/json")
                || resource.equals("/PodC/json") || resource.equals("/PodD/json")    ;
    }

    /**
     * writeHeadersToOutput handles writing http headers to the
     * clients output stream
     * @param writer
     * @param contentType
     * @param foundResource
     * @throws IOException
     */
    private void writeHeadersToOutput(OutputStream writer, String contentType, boolean foundResource) throws IOException {
        if (foundResource) writer.write("HTTP/1.1 200 OK\r\n".getBytes());
        else writer.write("HTTP/1.1 404 Not Found!\r\n".getBytes());
        writer.write(String.format("Content-Type: %s;\r\n", contentType).getBytes());
        writer.write("\r\n".getBytes());
    }
}
