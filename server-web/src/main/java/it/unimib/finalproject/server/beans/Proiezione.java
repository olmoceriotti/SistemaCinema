package it.unimib.finalproject.server.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Proiezione {
    @JsonProperty
    private String id;
    @JsonProperty("film id")
    private String filmId;
    @JsonProperty
    private LocalDateTime orario;
    @JsonProperty
    private String sala;
    @JsonProperty("totale posti occupati")
    private int totPostiOccupati;
    @JsonProperty("posti prenotati")
    private List<String> postiPrenotati;

    public Proiezione(){}

    public Proiezione(String id, String filmid, LocalDateTime orario, String sala){
        this.id = id;
        this.filmId = filmid;
        this.orario = orario;
        this.sala = sala;
        this.totPostiOccupati = 0;
        this.postiPrenotati = new ArrayList<String>();
    }

    public String getId(){
        return this.id;
    }

    public boolean checkDisponibilitàPosti(List<String> posti){
        for (String posto : posti) {
            if (postiPrenotati.contains(posto)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPostiOccupati(List<String> posti){
        for (String posto : posti) {
            if (!postiPrenotati.contains(posto)){
                return false;
            }
        }
        return true;
    }

    public void addPostiOccupati(List<String> posti){
        this.postiPrenotati.addAll(posti);
        this.totPostiOccupati = this.postiPrenotati.size();
    }

    public boolean removePostiOccupati(String posti){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList<String> listaPosti = mapper.readValue(posti, ArrayList.class);
            for (String posto : listaPosti) {
                if(this.postiPrenotati.contains(posto)){
                    this.postiPrenotati.remove(posto);
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

    public static ArrayList<Proiezione> buildFromStringList(String list){
        int end;
        ArrayList<Proiezione> objList = new ArrayList<Proiezione>();
        while((end = list.indexOf(";")) != -1){
            String obj = list.substring(0, end);
            list = list.substring(end +1);
            int divider = obj.indexOf("/");
            obj = obj.substring(divider +1);
            objList.add(buildFromString(obj));
        }
        return objList;
    }

    public static Proiezione buildFromString(String proiezione){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            Proiezione p = mapper.readValue(proiezione, Proiezione.class);
            return p;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
