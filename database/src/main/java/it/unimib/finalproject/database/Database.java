package it.unimib.finalproject.database;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Database
**/

public class Database {
    private ConcurrentHashMap<String, String> database;
    private static final String FILENAME = "database.dat";
    private static DataOutputStream dos = null;
    private static DataInputStream dis = null;
    
    public Database(){
        database = new ConcurrentHashMap<>();
        try {
            dos = new DataOutputStream(new FileOutputStream(FILENAME, true));
            dis = new DataInputStream(new FileInputStream(FILENAME));
        } catch (Exception e) {
            System.out.println("Impossible to access Database Backup file");

        }
        restoreFromBackup();
        startSnapshotDaemon();
    }

    //CRUD functions
    public boolean create(String data){
        String[] pair = getKeyValue(data);
        if(pair == null){
            return false;
        }
        database.put(pair[0], pair[0]);
        return true;
    }

    public String read(String key){
        String value = database.get(key);
        if (value == null){
            System.out.println("Impossible to retrieve the requested data"); 
            return "ERRORE";
        }
        return value;
    }

    public boolean update(String data){
        String[] pair = getKeyValue(data);
        if(pair == null){
            return false;
        }
        String result = database.replace(pair[0], pair[1]);
        if (result == null){
            System.out.println("Impossible to modify a non-existing value");
            return false;
        }
        return true;
    }

    public boolean remove(String key){
        String value = database.remove(key);
        if(value == null){
            System.out.println("Impossible to delete the requested  data");
            return false;
        }
        return true;
    }

    //Backup functions
    private void startSnapshotDaemon(){
        Thread daemonThread = new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(1000);
                    saveSnapshot();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                
            }
        }); 
        daemonThread.setDaemon(true);
        daemonThread.start();
    }

    private void saveSnapshot(){
        Iterator<String> iterator = database.keySet().iterator();

        while(iterator.hasNext()){
            String key = iterator.next();
            StringBuilder sb = new StringBuilder();
            sb.append(key);
            sb.append(";");
            sb.append(database.get(key));
            if (dos != null){
                try {
                    dos.writeUTF(sb.toString());
                } catch (IOException e) {
                    System.out.println("Impossible to write on Database Backup file");
                }
            }
        }
    }

    private boolean restoreFromBackup(){
        BufferedReader br =  new BufferedReader(new InputStreamReader(dis));
        String data;
        try{
            while((data = br.readLine()) != null){
                String[] pair = getKeyValue(data);
                database.put(pair[0], pair[0]);
            }
        }catch(IOException e){
            System.out.println("Impossible to read from Database Backup file");
            return false;
        }
        return true;
    }
    
    //Utilities
    private String[] getKeyValue(String data){
        int divider = data.indexOf(";");
        if (divider == -1) {
            System.out.println("Data format not valid");
            return null;
        }
        String key = data.substring(0, divider);
        String value = data.substring(divider);
        String[] pair = {key, value};
        return pair;
    }
}