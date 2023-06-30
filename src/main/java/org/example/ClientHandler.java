package org.example;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable{

    private final Socket socket;

    final static List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html",
            "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
            while (true){
                Request request = createRequest(in, out);

                Handler handler = Server.getHandlers().get(request.getMethod()).get(request.getPath());
                System.out.println(handler);

                if (handler==null){
                    Path parent = Path.of(request.getPath()).getParent();
                    handler = Server.getHandlers().get(request.getMethod()).get(parent.toString());
                    if (handler == null){
                        error404NotFound(out);
                        return;
                    }
                }
                handler.handle(request,out);
                responseOK(request, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Request createRequest(BufferedReader in, BufferedOutputStream out) throws IOException {

        final String requestLine = in.readLine();
        final String[] parts = requestLine.split(" ");
        System.out.println(requestLine);

        if (parts.length != 3) {
            //просто закрываем socket
            socket.close();
        }
        final String path = parts[1];
        if (!validPaths.contains(path)) {
            error404NotFound(out);
        }
        String line;
        Map<String, String> headers = new HashMap<>();
        while (!(line= in.readLine()).equals("")){
            int indexOf = line.indexOf(":");
            String name = line.substring(0, indexOf);
            String value = line.substring(indexOf + 2);
            headers.put(name,value);
        }

        Request request = new Request(parts[0], parts[1] ,socket.getInputStream(), headers);
        System.out.println(request);
        out.flush();
        return request;


    }
    static void error404NotFound(BufferedOutputStream out) throws IOException {
            out.write((
                    "HTTP/1.1 404 Not Found \r\n" +
                                    "Content-Length: 0\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.flush();
    }
    static void responseOK(Request request, BufferedOutputStream responseStream) throws IOException {

        final Path filePath = Path.of(".", "public", request.getPath());
        final String mimeType = Files.probeContentType(filePath);

        final String template = Files.readString(filePath);
        final  byte[] content = template.replace(
                "{time}", LocalDateTime.now().toString()).getBytes();

        responseStream.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        responseStream.write(content);

        final Long length = Files.size(filePath);
        responseStream.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
                ).getBytes());
        Files.copy(filePath,responseStream);
        responseStream.flush();
    }
}
