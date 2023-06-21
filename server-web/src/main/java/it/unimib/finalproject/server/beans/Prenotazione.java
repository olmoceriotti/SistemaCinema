package it.unimib.finalproject.server.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Prenotazione {
    @JsonProperty
    private String id;
    //private Proiezione proiezione;
    @JsonProperty
    private String proiezioneID;
    @JsonProperty
    private int numeroPosti;
    @JsonProperty
    private List<String> posti;

    public Prenotazione(){}

    public Prenotazione(String proiezioneID, List<String> posti){
        this.id = UUID.randomUUID().toString();
        this.proiezioneID = proiezioneID;
        this.posti = posti;
        this.numeroPosti = posti.size();
    }

    public String getID(){
        return this.id;
    }

    public String getProiezioneId(){
        return this.proiezioneID;
    }

    public List<String> getPosti(){
        List<String> copy = new ArrayList<String>(this.posti);
        return copy;
    }

    public boolean cancellaPosti(String posti){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList<String> listaPosti = mapper.readValue(posti, ArrayList.class);
            for (String posto : listaPosti) {
                if(this.posti.contains(posto)){
                    this.posti.remove(posto);
                }else{
                    return false;
                }
            }
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static Prenotazione buildFromString(String s){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Prenotazione p = mapper.readValue(s, Prenotazione.class);
            return p;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
