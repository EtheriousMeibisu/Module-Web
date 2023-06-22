package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Server {
    final int COUNT = 64;
    ExecutorService executorService = Executors.newFixedThreadPool(COUNT);
    public void startingTheServer(int port) {
                                                                                    //http://localhost:9999/index.html
        try (final ServerSocket serverSocket = new ServerSocket(9999)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                System.out.println(socket);
                Handler handler = new Handler(socket);
                executorService.submit(handler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
