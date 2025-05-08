
package chat;

import Objetos.DecoderMensaje;
import Objetos.EncoderMensaje;
import Objetos.Mensaje;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value="/chat_colab", encoders = {EncoderMensaje.class},decoders = {DecoderMensaje.class}) 

public class MiChat {
    
    private static final List<Session> conectados = new ArrayList<>();
    
    @OnOpen
    public void inicio(Session sesion){
        conectados.add(sesion);
    }
    @OnClose
    public void salir(Session sesion){
        conectados.remove(sesion);
    }
    @OnMessage
    public void mensaje(Mensaje mensaje) throws IOException, EncodeException{
        for (Session sesion : conectados){
            sesion.getBasicRemote().sendObject(mensaje);
        }
    }
    
}
