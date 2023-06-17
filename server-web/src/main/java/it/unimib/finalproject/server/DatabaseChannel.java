package it.unimib.finalproject.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class DatabaseChannel {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public DatabaseChannel(){
        try {
            socket = new Socket("localhost", 3030);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        while (message != "") {
            int end = message.indexOf("+");
            String chunk = "";
            if (end != -1) {
                chunk = message.substring(0, end);
                message = message.substring(end + 1);
                out.print(chunk + "\n");
            } else {
                out.print(message + "\n");
                message = "";
            }
            out.flush();
        }
    }

    public String receiveMessage() throws IOException{
        StringBuilder sb = new StringBuilder();
        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            if ("#".equals(inputLine)) {
                break;
            }
            sb.append(inputLine);
        }
        return sb.toString();
    }
}
