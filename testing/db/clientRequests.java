package db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clientRequests {
    public static void main(String[] args) {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            socket = new Socket("localhost", 3030);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String command = "CREATE str:12345 / valore ; #";

        while(command != ""){
            int end = command.indexOf(" ");
            String message = "";
            if(end != -1){
                message = command.substring(0, end);
                command = command.substring(end + 1);
                out.print(message + "\n");
            }else{
                out.print(command + "\n");
            }
        }
    }
}
