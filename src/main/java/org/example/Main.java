package org.example;

import javax.management.Query;
import java.io.IOException;
public class Main {
    final static int PORT = 9999;
    public static void main(String[] args) {

        Server server = new Server();

        for (String validPath : ClientHandler.validPaths){
            server.addHandler("GET", validPath, (request, responseStream) -> {
                try{
                    ClientHandler.responseOK(request,responseStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        server.addHandler("POST", "/resources.html", (request, responseStream) -> {
           try{
               ClientHandler.responseOK(request,responseStream);
            }catch (IOException e){
               e.printStackTrace();
           }
        });
        server.startingTheServer(PORT);


    }
}