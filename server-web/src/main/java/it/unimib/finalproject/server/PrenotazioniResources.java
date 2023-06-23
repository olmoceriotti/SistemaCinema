package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;

import it.unimib.finalproject.server.beans.*;

@Path("prenotazione")
public class PrenotazioniResources {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPrenotazione(){
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.key_filter("prenotazione:");
        ArrayList<Prenotazione> list = Prenotazione.buildFromStringList(s);
        return Response.ok(list).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPrenotazione(@PathParam("id") int id, String body){
        Prenotazione pre = Prenotazione.buildFromString(body);
        pre.setId();
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.read("proiezione:" + pre.getProiezioneID());
        Proiezione pro = Proiezione.buildFromString(s);
        System.out.println(pro.toString());
        System.out.println(pre.toString());
        if(pro.checkDisponibilitàPosti(pre.getPosti())){
            prtcl.create("prenotazione:" + pre.getID(), pre.toString());
            pro.addPostiOccupati(pre.getPosti());
            prtcl.update("proiezione:" + pro.getId(), pro.toString());
            return Response.ok(pre.getID()).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") String id){
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.read("prenotazione:" + id);
        Prenotazione p = Prenotazione.buildFromString(s);
        if(p != null){
            return Response.ok(p).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyPrenotazione(@PathParam("id") String id, String body){
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.read("prenotazione:" + id);
        Prenotazione p = Prenotazione.buildFromString(s);
        String s1 = prtcl.read("proiezione:" + p.getProiezioneID());
        Proiezione pro = Proiezione.buildFromString(s1);
        if(p.cancellaPosti(body, pro)){
            prtcl.update("prenotazione:" + p.getID(), p.toString());
            prtcl.update("proiezione:" + pro.getId(), pro.toString());
            return Response.ok(p.getID()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrenotazione(@PathParam("id") String id){
        ProtocolHandler prtcl = new ProtocolHandler();
        String p = prtcl.read("prenotazione:" + id);
        Prenotazione pre = Prenotazione.buildFromString(p);
        p = prtcl.read("proiezione:" + pre.getProiezioneID());
        Proiezione pro  = Proiezione.buildFromString(p);
        if(pro.removePostiOccupati(pre.getPosti())){
            if(prtcl.delete("prenotazione:" + id)){
                prtcl.update("proiezione:" + pro.getId(), pro.toString());
                return Response.noContent().build();
            }
            
        };
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
