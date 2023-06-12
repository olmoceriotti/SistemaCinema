package it.unimib.finalproject.database;

import java.net.*;
import java.io.*;

public class Main {

    public static final int PORT = 3030;

    public static void startServer() {
        ServerSocket server;
        try {
            server = new ServerSocket(PORT);

            System.out.println("Database listening at localhost:" + PORT);
            
            while (true){
                new RequestHandler(server.accept()).start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
            
    }
    
    public static void main(String[] args) throws IOException {
        startServer();
    }
}

