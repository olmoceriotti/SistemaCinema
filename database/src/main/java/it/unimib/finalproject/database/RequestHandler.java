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
            System.out.println("No clue");
        }
        
    }

    public void run() {
        try {
            if(receiveMessage()){
                boolean outcome = protocol.execute();
                sendMessage(protocol.responseBuilder(outcome));
                protocol.reset();
            }else{
                sendMessage("ERRORE");
            }
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private boolean receiveMessage() throws IOException{
        String inputLine;
        boolean validator = false;
        while ((inputLine = in.readLine()) != null) {
            if(";".equals(inputLine)){
                System.out.println("Connection closed");
                return true;
            }
            validator = protocol.readInput(inputLine);
            if(!validator){
                System.out.println("A problem occurred on the keyword: " +  inputLine);
                return false;
            };
        }
        return false;
    }

    private void sendMessage(String message){
        out.print(message);
        out.flush();
    }
}
