package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

import it.unimib.finalproject.server.classes.*;

@Path("film")
public class FilmsResources {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilms(){
        ProtocolHandler prtcl = new ProtocolHandler();
        String films = prtcl.key_filter("film:");
        List<Film> list = Film.fromStringToObjects(films);
        return Response.ok(list).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilmInfo(@PathParam("id") int id){
        ProtocolHandler prtcl = new ProtocolHandler();
        String film = prtcl.read("film:"+ id);
        Film filmObj = Film.buildFromString(film);
        if(filmObj != null){
            return Response.ok(filmObj).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
