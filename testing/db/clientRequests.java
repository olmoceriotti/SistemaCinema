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
        String command = "";
        try {
            socket = new Socket("localhost", 3030);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (args.length != 0) {
            for(int i = 0; i < args.length; i++){
                command += args[i] + " ";
            }
            command += "#";
        }else{
            command = "CREATE ciao:0 / 7 ; #";
        }
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

        System.out.println("command sent my dawg");
        StringBuilder sb = new StringBuilder();
        while((inputLine = in.readLine()) != null){
            if("#".equals(inputLine)){
                break;
            }
            sb.append(inputLine);
        }
        System.out.println(sb.toString());
    }
}
