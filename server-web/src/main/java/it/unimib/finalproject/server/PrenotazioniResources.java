package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;
import java.util.UUID;

import it.unimib.finalproject.server.classes.*;

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
        String token = UUID.randomUUID().toString();
        Prenotazione pre = Prenotazione.buildFromString(body);
        if(pre == null || pre.getPosti().isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        pre.setId();
        pre.setNumeroPosti();
        ProtocolHandler prtcl = new ProtocolHandler();
        for(int i = 0; i< 500; i++){
            if(prtcl.lock(token, "proiezione:" + pre.getProiezioneID())){
                String s = prtcl.read("proiezione:" + pre.getProiezioneID());
                Proiezione pro = Proiezione.buildFromString(s);
                if(pro.checkDisponibilitàPosti(pre.getPosti())){
                    prtcl.create("prenotazione:" + pre.getID(), pre.toString());
                    pro.addPostiOccupati(pre.getPosti());
                    prtcl.update(token, "proiezione:" + pro.getId(), pro.toString());
                    prtcl.unlock(token, "proiezione:" + pre.getProiezioneID());
                    return Response.status(Response.Status.CREATED).entity(pre.getID()).build();
                }else{
                    prtcl.unlock(token, "proiezione:" + pre.getProiezioneID());
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }else{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();  
    }
    
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") String id){
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.read("prenotazione:" + id);
        if(s != null){
            Prenotazione p = Prenotazione.buildFromString(s);
            return Response.ok(p).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyPrenotazione(@PathParam("id") String id, String body){
        String token = UUID.randomUUID().toString();
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.read("prenotazione:" + id);
        Prenotazione p = Prenotazione.buildFromString(s);
        if(p == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ArrayList<String> keys = new ArrayList<>();
        keys.add("prenotazione:" + id);
        keys.add("proiezione:" + p.getProiezioneID());
        for(int i = 0; i < 500; i++){
            if(prtcl.lock(token, keys)){
                String s1 = prtcl.read("proiezione:" + p.getProiezioneID());
                Proiezione pro = Proiezione.buildFromString(s1);
                if(p.cancellaPosti(body, pro)){
                    prtcl.update(token, "prenotazione:" + p.getID(), p.toString());
                    prtcl.update(token, "proiezione:" + pro.getId(), pro.toString());
                    prtcl.unlock(token, keys);
                    return Response.noContent().build();
                }else{
                    prtcl.unlock(token, keys);
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }else{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } 
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
        

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrenotazione(@PathParam("id") String id){
        String token = UUID.randomUUID().toString();
        ProtocolHandler prtcl = new ProtocolHandler();
        String p = prtcl.read("prenotazione:" + id);
        Prenotazione pre = Prenotazione.buildFromString(p);
        if(pre != null){
            p = prtcl.read("proiezione:" + pre.getProiezioneID());
            Proiezione pro  = Proiezione.buildFromString(p);
            if(pro.removePostiOccupati(pre.getPosti())){
                if(prtcl.delete(token, "prenotazione:" + id)){
                    prtcl.update(token, "proiezione:" + pro.getId(), pro.toString());
                    return Response.noContent().build();
                } 
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
