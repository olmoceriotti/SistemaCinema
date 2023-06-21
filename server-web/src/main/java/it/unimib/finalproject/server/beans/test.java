package it.unimib.finalproject.server.beans;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.unimib.finalproject.server.ProtocolHandler;

public class test {
    private static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws JsonProcessingException, URISyntaxException {
        populateFilms();
        populateProiezione();

    }

    private static void populateFilms() throws JsonProcessingException, URISyntaxException{
        ArrayList<Film> films = new ArrayList<Film>();
        films.add(new Film("1","Super Mario Bros - Il film", 92, "Chris Pratt, Anya Taylor Joy, Jack Black", "Aaron Horvath", "502356"));
        films.add(new Film("2","The Flash", 144, "Ezra Miller, Ben Affleck, Sasha Calle", "Andy Muschietti", "298618"));
        films.add(new Film("3","La Sirenetta", 135, "Halle Bailey, Jonah Hauer-king, Melissa McCarthy", "Rob Marshall", "447277"));
        films.add(new Film("4","Fast X", 141, "Vin Diesel, Michelle Rodriguez, Jason Statham", "Louis Leterrier", "385687"));
        
        for (Film film : films) {
            ProtocolHandler pch = new ProtocolHandler();
            String s = mapper.writeValueAsString(film);
            if(pch.create("film:" + film.getId(), s)){
                System.out.println("Added " + film.getId());
            }else{
                System.out.println("SAD");
                break;
            }
        }
    }

    private static void populateProiezione() throws JsonProcessingException{
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ArrayList<Proiezione> proiezioni = new ArrayList<Proiezione>();
        proiezioni.add(new Proiezione("1", "1", LocalDateTime.of(2023, 7, 12, 20, 30), "U1"));
        proiezioni.add(new Proiezione("2", "2", LocalDateTime.of(2023, 7, 12, 21, 30), "U2"));
        proiezioni.add(new Proiezione("3", "3", LocalDateTime.of(2023, 7, 12, 22, 30), "U1"));
        proiezioni.add(new Proiezione("4", "4", LocalDateTime.of(2023, 7, 12, 23, 30), "U2"));
        proiezioni.add(new Proiezione("5", "1", LocalDateTime.of(2023, 7, 13, 20, 30), "U1"));
        proiezioni.add(new Proiezione("6", "2", LocalDateTime.of(2023, 7, 13, 21, 30), "U2"));
        proiezioni.add(new Proiezione("7", "3", LocalDateTime.of(2023, 7, 13, 22, 30), "U1"));
        proiezioni.add(new Proiezione("8", "4", LocalDateTime.of(2023, 7, 13, 23, 30), "U2"));
        proiezioni.add(new Proiezione("9", "1", LocalDateTime.of(2023, 7, 14, 20, 30), "U1"));
        proiezioni.add(new Proiezione("10", "2", LocalDateTime.of(2023, 7, 14, 21, 30), "U2"));
        proiezioni.add(new Proiezione("11", "3", LocalDateTime.of(2023, 7, 14, 22, 30), "U1"));
        proiezioni.add(new Proiezione("12", "4", LocalDateTime.of(2023, 7, 14, 23, 30), "U2"));
        for (Proiezione proiezione : proiezioni) {
            ProtocolHandler prtcl = new ProtocolHandler();
            String s = mapper.writeValueAsString(proiezione);
            if(prtcl.create("proiezione:" + proiezione.getId(), s)){
                System.out.println("Added " + proiezione.getId());
            }else{
                System.out.println("SAD on " + proiezione.getId());
                break;
            }
        }
    }
}
