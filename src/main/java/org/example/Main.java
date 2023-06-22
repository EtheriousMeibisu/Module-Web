package org.example;

public class Main {
    final static int PORT = 9999;

    public static void main(String[] args) {

        Server server = new Server();
        server.startingTheServer(PORT);
    }
}