package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class StringMessageBodyWriter implements MessageBodyWriter<String> {

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return type == String.class && MediaType.APPLICATION_JSON_TYPE.equals(mediaType);
    }

    @Override
    public void writeTo(final String t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream out) throws IOException, WebApplicationException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(out, t);
    }
}
