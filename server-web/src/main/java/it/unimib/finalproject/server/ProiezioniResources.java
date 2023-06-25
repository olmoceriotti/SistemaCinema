package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


import java.util.ArrayList;
import java.util.UUID;


import it.unimib.finalproject.server.beans.*;

@Path("proiezione")
public class ProiezioniResources {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProiezioni(){
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.key_filter("proiezione:");
        ArrayList<Proiezione> list = Proiezione.buildFromStringList(s);
        return Response.ok(list).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProiezione(@PathParam("id") int id){
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
    public Response deleteProiezione(@PathParam("id") int id){
        String token = UUID.randomUUID().toString();
        ProtocolHandler prtcl = new ProtocolHandler();
        if(prtcl.delete(token, "proiezione:" + id))
            return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
