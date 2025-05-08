package Objetos;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;
import java.io.IOException;
import java.io.Reader;

/**
 * @author HP
 */
public class DecoderMensaje implements Decoder.TextStream<Mensaje>{

    @Override
    public Mensaje decode(Reader reader) throws DecodeException, IOException {
        Mensaje mensaje = new Mensaje();
        try(JsonReader jsonReader = Json.createReader(reader)){
            JsonObject json = jsonReader.readObject();
            mensaje.setNombre(json.getString("nombre"));
            mensaje.setMensaje(json.getString("mensaje"));
        }
        return mensaje;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

 
}
