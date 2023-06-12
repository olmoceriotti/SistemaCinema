package db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clientRequests {
    public static void main(String[] args) throws InterruptedException, IOException {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String inputLine;
        try {
            socket = new Socket("localhost", 3030);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String command = "READ ciao:2 ; #";
        while(command != ""){
            int end = command.indexOf(" ");
            String message = "";
            if(end != -1){
                message = command.substring(0, end);
                command = command.substring(end + 1);
                out.print(message + "\n");
            }else{
                out.print(command + "\n");    
                command = "";
            }
            out.flush();
        }

        while((inputLine = in.readLine()) != null){
            if("#".equals(inputLine)){
                break;
            }
            System.out.println(inputLine);
        }

        System.out.println("command sent my dawg");
    }
}
