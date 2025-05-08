
package Objetos;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonWriter;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import java.io.IOException;
import java.io.Writer;

/**
 * @author HP
 */
public class EncoderMensaje implements Encoder.TextStream<Mensaje> {
  
    @Override
    public void encode(Mensaje object, Writer writer) throws EncodeException, IOException {
        JsonObject json = Json.createObjectBuilder().add("nombre",object.getNombre()).add("mensaje",object.getMensaje()).build();
        try(JsonWriter jsonWriter = Json.createWriter(writer)){
            jsonWriter.writeObject(json);
        }
        
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}
