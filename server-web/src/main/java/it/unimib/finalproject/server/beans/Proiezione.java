package it.unimib.finalproject.server.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Proiezione {
    @JsonProperty
    private String id;
    private Film film;
    @JsonProperty("film id")
    private String filmId;
    @JsonProperty
    private LocalDateTime orario;
    @JsonProperty
    private Sala sala;
    @JsonProperty("totale posti occupati")
    private int totPostiOccupati;
    @JsonProperty("posti prenotati")
    private List<String> postiPrenotati;

    public Proiezione(){}

    public Proiezione(String id, Film film, LocalDateTime orario, Sala sala){
        this.id = id;
        this.film = film;
        this.filmId = film.getId();
        this.orario = orario;
        this.sala = sala;
        this.totPostiOccupati = 0;
        this.postiPrenotati = new ArrayList<String>();
    }

    public static Proiezione buildFromString(String proiezione){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Proiezione p = mapper.readValue(proiezione, Proiezione.class);
            return p;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
