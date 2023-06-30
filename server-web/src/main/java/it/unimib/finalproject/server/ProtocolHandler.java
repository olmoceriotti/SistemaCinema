package it.unimib.finalproject.server;

import java.io.IOException;
import java.util.ArrayList;

public class ProtocolHandler {

    public ProtocolHandler(){
    }
    
    public boolean create(String key, String value){
        String command = "CREATE+";
        command += key + "+" + value;
        command += "+;";
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
        command += "+;";
        String response = executeCommand(command);
        if (response != null) {
            if(response.contains("OK")){
                String value = response.substring(2);
                return value;
            }
        }
        return null;
    }

    public boolean update(String key, String value){
        return update(null, key, value);
    }

    public boolean update(String owner, String key, String value) {
        String command  = "";
        if(owner != null){
            command += owner + "+";
        }
       command += "UPDATE+";
       command += key + "+" + value;
       command += "+;";
       String response = executeCommand(command);
       if(response != null){
           if(response.contains("OK"))
            return true;
       }
       return false;
    }

    public boolean delete(String key){
        return delete(null, key);
    }

    public boolean delete(String owner, String key) {
        String command  = "";
        if(owner != null){
            command += owner + "+";
        }
        command = "DELETE+";
        command += key;
        command += "+;";
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
        command += "+;";
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
        command += "+;";
        String response = executeCommand(command);
        if (response != null) {
            if (response.contains("OK")) {
                String value = response.substring(2);
                return value;
            }
        }
        return null;
    }

    public boolean lock(String owner, String key){
        String command = owner + "+LOCK+";
        command += key;
        command += "+;";
        String response = executeCommand(command);
        if (response != null) {
            if (response.contains("OK")) {
                return true;
            }
        }
        return false;
    }

    public boolean lock(String owner, ArrayList<String> keys){
        String key = String.join("--", keys);
        String command = owner + "+LOCK+";
        command += key;
        command += "+;";
        String response = executeCommand(command);
        if (response != null) {
            if (response.contains("OK")) {
                return true;
            }
        }
        return false;
    }

    public boolean unlock(String owner, String key){
        String command = owner + "+UNLOCK+";
        command += key;
        command += "+;";
        String response = executeCommand(command);
        if (response != null) {
            if (response.contains("OK")) {
                return true;
            }
        }
        return false;
    }

    public boolean unlock(String owner, ArrayList<String> keys){
        String key = String.join("--", keys);
        String command = owner + "+UNLOCK+";
        command += key;
        command += "+;";
        String response = executeCommand(command);
        if (response != null) {
            if (response.contains("OK")) {
                return true;
            }
        }
        return false;
    }

    private String executeCommand(String command){
        DatabaseChannel dbChan = new DatabaseChannel();
        dbChan.sendMessage(command);
        try {
            String response = dbChan.receiveMessage();
            //System.out.println("ProtocolHandelr:162: " + response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
