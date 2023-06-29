package it.unimib.finalproject.server.beans;

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
        //populateProiezione();
    }

    private static void populateFilms() throws JsonProcessingException, URISyntaxException{
        ArrayList<Film> films = new ArrayList<Film>();
        /*films.add(new Film("1","Super Mario Bros - Il film", 92, "Chris Pratt, Anya Taylor Joy, Jack Black", "Aaron Horvath", "502356"));
        films.add(new Film("2","The Flash", 144, "Ezra Miller, Ben Affleck, Sasha Calle", "Andy Muschietti", "298618"));
        films.add(new Film("3","La Sirenetta", 135, "Halle Bailey, Jonah Hauer-king, Melissa McCarthy", "Rob Marshall", "447277"));
        films.add(new Film("4","Fast X", 141, "Vin Diesel, Michelle Rodriguez, Jason Statham", "Louis Leterrier", "385687"));
        
        films.add(new Film("5","Transformers - Il Risveglio", 127, "Anthony Ramos, Dominique Fishback, Luna Lauren Velez", "Steven Caple Jr.", "667538"));*/
        films.add(new Film("6","Elemental", 102, "Leah Lewis, Mamoudou Athie, Ronnie del Carmen", "Peter Sohn", "976573"));
        /*films.add(new Film("7","Shazam! Furia degli dei", 130, "Zachary Levi, Asher Angel, Jack Dylan Grazer", "David F. Sandberg", "594767"));
        films.add(new Film("8","Fidanzata in affitto", 103, "Jennifer Lawrence, Andrew Barth Feldman, Laura Benanti", "Gene Stupnitsky", "884605"));*/

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
         proiezioni.add(new Proiezione("1", "1", LocalDateTime.of(2023, 7, 7, 19, 00), "U1"));
        proiezioni.add(new Proiezione("2", "2", LocalDateTime.of(2023, 7, 7, 20, 30), "U2"));
        proiezioni.add(new Proiezione("3", "3", LocalDateTime.of(2023, 7, 7, 17, 45), "U3"));
        proiezioni.add(new Proiezione("4", "4", LocalDateTime.of(2023, 7, 7, 22, 15), "U4"));
        proiezioni.add(new Proiezione("5", "5", LocalDateTime.of(2023, 7, 7, 20, 30), "U5"));
        proiezioni.add(new Proiezione("6", "6", LocalDateTime.of(2023, 7, 8, 17, 00), "U1"));
        proiezioni.add(new Proiezione("7", "7", LocalDateTime.of(2023, 7, 8, 21, 30), "U2"));
        proiezioni.add(new Proiezione("8", "8", LocalDateTime.of(2023, 7, 8, 22, 20), "U3"));
        proiezioni.add(new Proiezione("9", "3", LocalDateTime.of(2023, 7, 8, 19, 00), "U4"));
        proiezioni.add(new Proiezione("10", "2", LocalDateTime.of(2023, 7, 8, 23, 30), "U5"));
        proiezioni.add(new Proiezione("11", "6", LocalDateTime.of(2023, 7, 9, 20, 30), "U1"));
        proiezioni.add(new Proiezione("12", "5", LocalDateTime.of(2023, 7, 9, 23, 00), "U2"));
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
