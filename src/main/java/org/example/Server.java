package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Server {
    final int COUNT = 64;
    ExecutorService executorService = Executors.newFixedThreadPool(COUNT);
    private final static Map<String, Map<String,Handler>> handlers= new ConcurrentHashMap<>();
    public void startingTheServer(int port) {
                                                                                    //http://localhost:9999/index.html
        try (final ServerSocket serverSocket = new ServerSocket(9999)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                System.out.println(socket);
                ClientHandler clientHandler = new ClientHandler(socket);
                executorService.submit(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addHandler(String method,String path, Handler handler ){
        if (handlers.containsKey(method)){
            handlers.get(method).put(path,handler);
        }else {
            handlers.put(method, new ConcurrentHashMap<>(Map.of(path,handler)));
        }
        //System.out.println(handlers);
    }
    public static Map <String, Map<String, Handler>> getHandlers(){
        return handlers;
    }
}
