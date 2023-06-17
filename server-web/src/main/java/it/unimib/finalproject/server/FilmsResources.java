package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;

import java.util.List;

import com.fasterxml.jackson.core.*;

import it.unimib.finalproject.server.beans.*;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFilm(){
        //parse body data e (CREATE "film:UUIDv4" "json body")
        return null;
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

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFilm(@PathParam("id") int id){
        ProtocolHandler prtcl = new ProtocolHandler();
        if(prtcl.delete("film:" + id))
            return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
