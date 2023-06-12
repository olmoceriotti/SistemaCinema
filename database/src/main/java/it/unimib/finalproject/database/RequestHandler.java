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
                System.out.println(validator + " " + inputLine);
                if(!validator){
                    System.out.println("A problem occurred on the keyword: " +  inputLine);
                    break;
                };
                //out.println(inputLine);
            }
            System.out.println("Ricevuto capo");
            if(validator){
                boolean outcome = protocol.execute();
                sendMessage(protocol.responseBuilder(outcome));
                protocol.reset();
            }else{
                System.out.println("errore?");
                sendMessage("ERRORE");
            }
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void sendMessage(String message){
        while (message != "") {
            int end = message.indexOf(" ");
            String chunk = "";
            if (end != -1) {
                chunk = message.substring(0, end);
                message = message.substring(end + 1);
                out.print(chunk + "\n");
                out.flush();
            } else {
                out.print(message + "\n");
                message = "";
            }
        }
    }
}
