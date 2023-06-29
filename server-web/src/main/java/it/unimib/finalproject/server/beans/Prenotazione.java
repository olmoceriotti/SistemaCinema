package it.unimib.finalproject.server.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Prenotazione {
    @JsonProperty
    private String id;
    @JsonProperty
    private String proiezioneID;
    @JsonProperty
    private int numeroPosti;
    @JsonProperty
    private List<String> posti;

    public Prenotazione(){
    }

    public Prenotazione(String proiezioneID, List<String> posti){
        this.id = UUID.randomUUID().toString();
        this.proiezioneID = proiezioneID;
        this.posti = posti;
        this.numeroPosti = posti.size();
    }

    public String getID(){
        return this.id;
    }

    public String getProiezioneID(){
        return this.proiezioneID;
    }

    public void setId(){
        if(this.id == null){
            this.id = UUID.randomUUID().toString();
        }
    }

    public void setNumeroPosti(){
        this.numeroPosti = this.posti.size();
    }

    public List<String> getPosti(){
        List<String> copy = new ArrayList<String>(this.posti);
        return copy;
    }

    public boolean cancellaPosti(String posti, Proiezione pro){
        ObjectMapper mapper = new ObjectMapper();
        boolean check1 = true;
        boolean check2 = true;
        try {
            ArrayList<String> listaPosti = mapper.readValue(posti, new TypeReference<ArrayList<String>>() {});

            for (String posto : listaPosti) {
                if(!this.posti.contains(posto) || !Proiezione.isPosto(posto)){
                    check1 = false;
                }
            }

            if(check1){
                check2 = pro.checkPostiOccupati(listaPosti);
            }else{
                return false;
            }

            if(check2){
                for (String posto : listaPosti) {
                    if(this.posti.contains(posto)){
                        this.posti.remove(posto);
                    }
                }
                pro.removePostiOccupati(posti);
                this.numeroPosti = this.posti.size();
                return true;
            }else{
                return false;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static ArrayList<Prenotazione> buildFromStringList(String list){
        int end;
        ArrayList<Prenotazione> objList = new ArrayList<Prenotazione>();
        while((end = list.indexOf(";")) != -1){
            String obj = list.substring(0, end);
            list = list.substring(end +1);
            int divider = obj.indexOf("/");
            obj = obj.substring(divider +1);
            objList.add(buildFromString(obj));
        }
        return objList;
    }

    public static Prenotazione buildFromString(String prenotazione){
        ObjectMapper mapper = new ObjectMapper();
        try {
            if(prenotazione !=  null){
                Prenotazione p = mapper.readValue(prenotazione, Prenotazione.class);
                return p;
            }
        } catch (JsonProcessingException e) {}
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
