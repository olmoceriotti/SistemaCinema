package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import it.unimib.finalproject.server.beans.*;

@Path("prenotazione")
public class PrenotazioniResources {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPrenotazione(@PathParam("id") int id, String body){
        Prenotazione pre = Prenotazione.buildFromString(body);
        ProtocolHandler prtcl = new ProtocolHandler();
        String s  = prtcl.read("proiezione:"+pre.getProiezioneId());
        Proiezione pro = Proiezione.buildFromString(s);
        if(pro.checkDisponibilitàPosti(pre.getPosti())){
            prtcl.create("prenotazione:" + pre.getID(), body);
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
    public Response getPrenotazione(@PathParam("id") int id){
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.read("prenotazione:" + id);
        Prenotazione p = Prenotazione.buildFromString(s);
        if(p != null){
            return Response.ok(s).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyPrenotazione(@PathParam("id") int id, String body){
        //Body lista posti
        ProtocolHandler prtcl = new ProtocolHandler();
        String s = prtcl.read("prenotazione:" + id);
        Prenotazione p = Prenotazione.buildFromString(s);
        String s1 = prtcl.read("proiezione:" + p.getProiezioneId());
        Proiezione pro = Proiezione.buildFromString(s1);
        //TODO ADAPTER CANCELLA POSTI UNICA OPERAZIONE
        if(p.cancellaPosti(body)){
            if(pro.removePostiOccupati(body)){
                prtcl.update("prenotazione:" + p.getID(), p.toString());
                prtcl.update("proiezione:" + pro.getId(), pro.toString());
                return Response.noContent().build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrenotazione(@PathParam("id") int id){
        ProtocolHandler prtcl = new ProtocolHandler();
        if(prtcl.delete("prenotazione:" + id))
            return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
