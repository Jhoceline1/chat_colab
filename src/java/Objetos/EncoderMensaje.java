package Objetos;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

import java.io.IOException;
import java.io.Writer;

public class EncoderMensaje implements Encoder.TextStream<Mensaje> {
    @Override
    public void encode(Mensaje object, Writer writer) throws EncodeException, IOException {
        JsonObjectBuilder builder = Json.createObjectBuilder()
            .add("tipo", object.getTipo() == null ? "mensaje" : object.getTipo())
            .add("nombre", object.getNombre() == null ? "" : object.getNombre());

        if ("usuarios".equals(object.getTipo()) && object.getLista() != null) {
            builder.add("lista", Json.createArrayBuilder(object.getLista()));
        }

        if (object.getMensaje() != null) builder.add("mensaje", object.getMensaje());
        if (object.getEstado() != null) builder.add("estado", object.getEstado());

        try (JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.writeObject(builder.build());
        }
    }

    @Override public void init(EndpointConfig config) {}
    @Override public void destroy() {}
}
