package it.unimib.finalproject.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler extends Thread {
    private Socket client;
    private ProtocolHandler protocol;
    private PrintWriter out;
    private BufferedReader in;
    public RequestHandler(Socket client) {
        this.client = client;
        protocol = new ProtocolHandler();
        try{
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }catch(IOException e){
            
        }
        
    }

    public void run() {
        try {
            String inputLine;
            boolean validator = false;
            while ((inputLine = in.readLine()) != null) {
                if("#".equals(inputLine)){
                    out.println("Connection closed");
                    break;
                }
                validator = protocol.readInput(inputLine);
                if(!validator){
                    System.out.println("A problem occurred on the keyword: " +  inputLine);
                    break;
                };
                //out.println(inputLine);
            }
            if(validator){
                if(protocol.execute()){
                    sendResponse(protocol.getOutput());
                }
                protocol.reset();
            }else{
                sendResponse("ERRORE");
            }
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void sendResponse(String response) {
        out.println(response);
    }
}
