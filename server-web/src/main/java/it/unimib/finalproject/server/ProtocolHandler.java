package it.unimib.finalproject.server;

import java.io.IOException;

public class ProtocolHandler {

    public ProtocolHandler(){
    }
    
    public boolean create(String key, String value){
        String command = "CREATE+";
        command += key + "+/+" + value;
        command += "+;+#";
        String response = executeCommand(command);
        if(response != null){
            if(response.contains("OK"))
                return true;
        }
        return false;
    }

    public String read(String key) {
        String command = "READ+";
        command += key;
        command += "+;+#";
        String response = executeCommand(command);
        if (response != null) {
            if(response.contains("OK")){
                String value = response.substring(2);
                return value;
            }
        }
        return null;
    }

    public boolean update(String key, String value) {
       String command = "UPDATE+";
       command += key + "+/+" + value;
       command += "+;+#";
       String response = executeCommand(command);
       if(response != null){
           if(response.contains("OK"))
            return true;
       }
       return false;
    }

    public boolean delete(String key) {
        String command = "DELETE+";
        command += key;
        command += "+;+#";
        String response = executeCommand(command);
        if (response != null) {
            if(response.contains("OK"))
                return true;
        }
        return false;
    }

    public boolean exists(String key) {
        String command = "EXISTS+";
        command += key;
        command += "+;+#";
        String response = executeCommand(command);
        if (response != null) {
            if(response.contains("OK"))
                return true;
        }
        return false;
    }

    public String key_filter(String filter){
        String command = "KEY_FILTER+";
        command += filter;
        command += "+;+#";
        String response = executeCommand(command);
        if (response != null) {
            if (response.contains("OK")) {
                String value = response.substring(2);
                return value;
            }
        }
        return null;
    }

    private String executeCommand(String command){
        DatabaseChannel dbChan = new DatabaseChannel();
        dbChan.sendMessage(command);
        try {
            String response = dbChan.receiveMessage();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
