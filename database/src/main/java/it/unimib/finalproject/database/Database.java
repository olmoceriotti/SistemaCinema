package it.unimib.finalproject.database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class Database {
    private HashMap<String, String> database;
    private HashMap<String, Set<String>> lockedKeys;
    
    private static final String FILENAME = "database.dat";
    private static DataOutputStream dos = null;
    private FileInputStream fis;
    private static DataInputStream dis = null;
    private static Database instance;

    private static boolean snapshots = true;

    private Database() throws IOException{
        database = new HashMap<>();
        lockedKeys = new HashMap<String, Set<String>>();
        try {
            fis = new FileInputStream(FILENAME);
            dis = new DataInputStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Impossible to access Database Backup file");
        }
        restoreFromBackup();
        System.out.println("backup restored");
        if(snapshots) startSnapshotDaemon();
    }

    //CRUD functions
    public synchronized boolean create(String key, String value){
        try{
            if(database.get(key) == null){
                database.put(key, value);
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

    public boolean update(String owner, String key, String value){
        boolean isImplicitLock = false;
        if(!isLocked(key)){
            if(owner == null){
                owner = UUID.randomUUID().toString();
            }
            isImplicitLock = true;
            lock(owner, key);
        }
        if(lockedKeys.keySet().contains(owner)  && lockedKeys.get(owner).contains(key)){
            String result = database.replace(key, value);
            if (result == null){
                System.out.println("Impossible to modify a non-existing value");
                return false;
            }
            if(isImplicitLock) unlock(owner, key);
            return true; 
        }
        return false;  
    }

    public boolean delete(String owner, String key){
        boolean isImplicitLock = false;
        if(!isLocked(key)){
            if(owner == null){
                owner = UUID.randomUUID().toString();
            }
            isImplicitLock = true;
            lock(owner, key);
        }
        if(lockedKeys.keySet().contains(owner) && lockedKeys.get(owner).contains(key)){
            String value = database.remove(key);
            if(value == null){
                System.out.println("Impossible to delete the requested data");
                return false;
            }
            if(isImplicitLock) unlock(owner, key);
            return true;
        }else{
            return false;
        }  
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
                output += key + "/" + this.read(key) + ";\n";
            }
        }
        output = output.replace("ERRORE", "");
        return output;
    }

    public synchronized boolean lock(String owner, String keys){
        if(owner == null || keys == null) return false;
        String[] list = keys.split("--");
        for(String key : list){
            if(isLocked(key)){
                return false;
            }
        }
        if(lockedKeys.get(owner) == null){
            lockedKeys.put(owner, new HashSet<>());
        }
        for(String key : list){
            lockedKeys.get(owner).add(key);
        }
        return true;
    }

    public synchronized boolean unlock(String owner, String keys){
        if(owner == null || keys == null) return false;
        String[] list = keys.split("--");
        for(String key : list){
            if(!lockedKeys.get(owner).contains(key)){
                return false;
            }

        }
        for(String key : list){
            lockedKeys.get(owner).remove(key);
        }
        return true;
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
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                
            }
        }); 
        daemonThread.setDaemon(true);
        daemonThread.start();
    }

    private void saveSnapshot() throws FileNotFoundException, IOException{
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
        dos.close();
    }

    private boolean restoreFromBackup(){
        String data;
        try{
            while((data = dis.readUTF()) != null){
                //System.out.println(data);
                String[] pair = getKeyValue(data);
                database.put(pair[0], pair[1]);
            }
        } catch (IOException e){
           return false;
        }
        return true;
    }
    
    //Utilities

    private boolean isLocked(String key){
        for(String o : lockedKeys.keySet()){
            if(lockedKeys.get(o).contains(key)){
                System.out.println("The key is locked");
                return true;
            }
        }
        return false;
    }

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
            try {
                instance = new Database();
            } catch (Exception e) {} 
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