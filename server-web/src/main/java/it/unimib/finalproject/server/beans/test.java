package it.unimib.finalproject.server.beans;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.glassfish.grizzly.http.Protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unimib.finalproject.server.ProtocolHandler;

public class test {
    public static void main(String[] args) throws JsonProcessingException, URISyntaxException {
        ArrayList<Film> films = new ArrayList<Film>();
        //films.add(new Film("1","Super Mario Bros - Il film", 92, "Chris Pratt, Anya Taylor Joy, Jack Black", "Aaron Horvath", new URI("https://image.tmdb.org/t/p/original/")));
        films.add(new Film("2","The Flash", 144, "Ezra Miller, Ben Affleck, Sasha Calle", "Andy Muschietti", new URI("https://image.tmdb.org/t/p/original/")));
        //films.add(new Film("3","La Sirenetta", 135, "Halle Bailey, Jonah Hauer-king, Melissa McCarthy", "Rob Marshall", new URI("https://image.tmdb.org/t/p/original/")));
        //films.add(new Film("4","Fast X", 141, "Vin Diesel, Michelle Rodriguez, Jason Statham", "Louis Leterrier", new URI("https://image.tmdb.org/t/p/original/")));
        ObjectMapper mapper = new ObjectMapper();
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
}
