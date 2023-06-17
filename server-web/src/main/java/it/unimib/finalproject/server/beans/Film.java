package it.unimib.finalproject.server.beans;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.*;

public class Film implements Serializable {
    @JsonProperty
    private String id;
    @JsonProperty
    private String nome;
    @JsonProperty
    private int durata;
    @JsonProperty
    private String attori;
    @JsonProperty
    private String regista;
    @JsonProperty
    private URI photo;

    public Film(){}

    public Film(String nome, int durata, String attori, String regista, URI photo){
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.durata = durata;
        this.attori = attori;
        this.regista = regista;
        this.photo = photo;
    }

    public Film(String id, String nome, int durata, String attori, String regista, URI photo){
        this.id = id;
        this.nome = nome;
        this.durata = durata;
        this.attori = attori;
        this.regista = regista;
        this.photo = photo;
    }

    public String getId(){
        return this.id;
    }

    public static List<Film> fromStringToObjects(String list){
        ObjectMapper mapper = new ObjectMapper();
        int end;
        ArrayList<Film> objList = new ArrayList<Film>();
        while((end = list.indexOf(";")) != -1){
            String obj = list.substring(0, end);
            list = list.substring(end +1);
            int divider = obj.indexOf("/");
            System.out.println(obj.substring(0, divider));
            obj = obj.substring(divider +1);
            try {
                Film film = mapper.readValue(obj, Film.class);
                objList.add(film);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return objList;
    }

    public static Film buildFromString(String film){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Film filmObj = mapper.readValue(film, Film.class);
            return filmObj;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
