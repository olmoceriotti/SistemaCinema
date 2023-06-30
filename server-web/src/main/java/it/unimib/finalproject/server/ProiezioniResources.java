package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;

import it.unimib.finalproject.server.classes.*;

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
}
