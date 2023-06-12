package it.unimib.finalproject.database;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private ConcurrentHashMap<String, String> database;
    private static final String FILENAME = "database.dat";
    private static DataOutputStream dos = null;
    private static DataInputStream dis = null;
    private static Database instance;

    private Database(){
        database = new ConcurrentHashMap<>();
        try {
            dis = new DataInputStream(new FileInputStream(FILENAME));
        } catch (Exception e) {
            System.out.println("Impossible to access Database Backup file");

        }
        restoreFromBackup();
        startSnapshotDaemon();
        printTable();
    }

    //CRUD functions

    //Aggiornare per concorrenza
    public boolean create(String key, String value){
        try{
            if(database.put(key, value) == null){
                return true;
            }
        }catch(NullPointerException e){
            System.out.println("CREATE: Key or Value are null");
        }
        return false;
    }

    public String read(String key){
        String value = database.get(key);
        if (value == null){
            System.out.println("Impossible to retrieve the requested data"); 
            return "ERRORE";
        }
        return value;
    }

    public boolean update(String key, String value){
        String result = database.replace(key, value);
        if (result == null){
            System.out.println("Impossible to modify a non-existing value");
            return false;
        }
        return true;
    }

    public boolean delete(String key){
        String value = database.remove(key);
        if(value == null){
            System.out.println("Impossible to delete the requested data");
            return false;
        }
        return true;
    }

    public boolean exists(String key){
        String value = database.get(key);
        return value != null;
    }

    public String key_filter(String filter){
        Iterator<String> i = database.keySet().iterator();
        String output = "";
        String key;
        while(i.hasNext()){
            key = i.next();
            if(key.contains(filter)){
                output += this.read(key) + ",";
            }
        }
        output = output.replace("ERRORE", "");
        return output;

    }

    //Backup functions
    private void startSnapshotDaemon(){
        Thread daemonThread = new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(2000);
                    saveSnapshot();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                    break;
                }
                
            }
        }); 
        daemonThread.setDaemon(true);
        daemonThread.start();
    }

    private void saveSnapshot() throws FileNotFoundException{
        Iterator<String> iterator = database.keySet().iterator();
        dos = new DataOutputStream(new FileOutputStream(FILENAME, false));
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
        String data;
        try{
            while((data = dis.readUTF()) != null){
                String[] pair = getKeyValue(data);
                database.put(pair[0], pair[1]);
            }
        } catch (EOFException e) {

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
        String value = data.substring(divider + 1);
        String[] pair = {key, value};
        return pair;
    }

    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }

    void printTable(){
        int i = 0;
        System.out.println("Table:");
        for (String key : database.keySet()) {
            String value = database.get(key);
            System.out.println(i + ": " + key + " " + value);
            i++;
        }
    }
}