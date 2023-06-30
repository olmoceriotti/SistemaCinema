package it.unimib.finalproject.database;

public class ProtocolHandler {
    private final int START = 1;
    private final int KEY = 2;
    private final int KEY_VALUE = 3;
    private final int VALUE = 4;
    private final int READY = 5;

    private int state;

    private String owner = null;
    private String command;
    private String key;
    private String value;

    private Database db;
    private String output;

    ProtocolHandler(){
        state = START;
        db = Database.getInstance();
    }

    boolean readInput(String input){
        boolean success = true;
        switch (this.state) {
            case START:
                success = commandReader(input);
                if(success) this.command = input;
            break;
            case KEY:
                this.key = input;
                this.state = READY;
            break;
            case KEY_VALUE:
                this.key = input;
                this.state = VALUE;
            break;
            case VALUE:
                this.value = input;
                this.state = READY;
            break;
        
            default:
                success = false;
            break;
        }
        return success;
    }

    private boolean commandReader(String input){
        if (input.equals("READ") || input.equals("DELETE") || input.equals("EXISTS") || input
                .equals("KEY_FILTER") || input.equals("LOCK") || input.equals("UNLOCK")) {
            state = KEY;
            return true;
        } else if (input.equals("CREATE") || input.equals("UPDATE")) {
            state = KEY_VALUE;
            return true;
        }else{
            owner = input;
            return true;
        }
    }

    boolean execute(){
        if(state != READY){

            return false;
        }
        boolean success = false;
        output = null;
        switch (command) {
            case "CREATE":
                success = db.create(key, value);
            break;
            case "READ":
                output = db.read(key);
                if(output != "ERRORE") success = true;
            break;
            case "UPDATE":
                success = db.update(owner, key, value);
            break;
            case "DELETE":
                success = db.delete(owner, key);
            break;
            case "EXISTS":
                output = db.exists(key) ? "True" : "False";
                success = true;
            break;
            case "KEY_FILTER":
                output = db.key_filter(key);
                success = true;
            break;
            case "LOCK":
                success = db.lock(owner, key);
            break;
            case "UNLOCK":
                success = db.unlock(owner, key);
            break;
            default:
                success = false;
            break;
        }

        return success;
    }

    String getOutput(){
        if(this.state == READY){
            return output;
        }else{
            return null;
        }
    }

    void reset(){
        this.owner = null;
        this.state = START;
        this.command = null;
        this.key = null;
        this.value = null;
    }

    String responseBuilder(boolean outcome){
        StringBuilder response = new StringBuilder();
        if(outcome){
            response.append("OK ");
        }else{
            response.append("FAILED ");
        }
        if(command.equals("READ") || command.equals("EXISTS") || command.equals("KEY_FILTER")){
            response.append(getOutput() + " ");
        }
        response.append(";");
        return response.toString();

    }
}


