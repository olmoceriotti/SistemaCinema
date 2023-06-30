package it.unimib.finalproject.server.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
            if (postiPrenotati.contains(posto)  || !isPosto(posto)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPostiOccupati(List<String> posti){
        for (String posto : posti) {
            if (!postiPrenotati.contains(posto) || !isPosto(posto)){
                return false;
            }
        }
        return true;
    }

    public boolean addPostiOccupati(List<String> posti){
        for(String posto : posti){
            if(!isPosto(posto)) return false;
        }
        this.postiPrenotati.addAll(posti);
        this.totPostiOccupati = this.postiPrenotati.size();
        return true;
    }

    public boolean removePostiOccupati(String posti){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList<String> listaPosti = mapper.readValue(posti, new TypeReference<ArrayList<String>>() {});
            return removePostiOccupati(listaPosti);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removePostiOccupati(List<String> listaPosti){
        for (String posto : listaPosti) {
            if(isPosto(posto) && this.postiPrenotati.contains(posto)){
                this.postiPrenotati.remove(posto);
            }else{
                return false;
            }
        }
        this.totPostiOccupati = this.postiPrenotati.size();
        return true;
        
    }

    static boolean isPosto(String posto){
        String regex = "^[A-O][0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(posto);
        return matcher.matches();
    }

    public static ArrayList<Proiezione> buildFromStringList(String list){
        int end;
        ArrayList<Proiezione> objList = new ArrayList<Proiezione>();
        while((end = list.indexOf("--")) != -1){
            String obj = list.substring(0, end);
            list = list.substring(end + 2);
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
            if(proiezione != null){
                Proiezione p = mapper.readValue(proiezione, Proiezione.class);
                return p;
            }
        } catch (JsonProcessingException e) {
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
