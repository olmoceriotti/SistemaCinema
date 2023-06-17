package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;

import java.util.List;

import com.fasterxml.jackson.core.*;

import it.unimib.finalproject.server.beans.*;

@Path("proiezione")
public class ProiezioniResources {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilms(){
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFilm(){
        return null;
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilmInfo(@PathParam("id") int id){
        ProtocolHandler prtcl = new ProtocolHandler();
        String proiezione = prtcl.read("proiezione:"+ id);
        Proiezione proiezioneObj = Proiezione.buildFromString(proiezione);
        if(proiezioneObj != null){
            return Response.ok(proiezioneObj).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFilm(@PathParam("id") int id){
        ProtocolHandler prtcl = new ProtocolHandler();
        if(prtcl.delete("proiezione:" + id))
            return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
