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

@ServerEndpoint(value="/chat_colab", encoders = {EncoderMensaje.class}, decoders = {DecoderMensaje.class}) 
public class MiChat {
    
    
    private static final List<Session> conectados = new ArrayList<>();
    

    @OnOpen
    public void inicio(Session sesion) throws IOException, EncodeException {
        
        conectados.add(sesion);
        
        
        Mensaje mensaje = new Mensaje();
        mensaje.setNombre("Sistema");
        mensaje.setMensaje("Un usuario se ha unido al chat.");
        
        
        for (Session s : conectados) {
            s.getBasicRemote().sendObject(mensaje);
        }
    }

    
    @OnClose
    public void salir(Session sesion) throws IOException, EncodeException {
        
        conectados.remove(sesion);
        
        
        Mensaje mensaje = new Mensaje();
        mensaje.setNombre("Sistema");
        mensaje.setMensaje("Un usuario ha salido del chat.");
        
        
        for (Session s : conectados) {
            s.getBasicRemote().sendObject(mensaje);
        }
    }

    // Método llamado cuando se recibe un mensaje de un usuario
    @OnMessage
    public void mensaje(Mensaje mensaje) throws IOException, EncodeException {
        // Enviar el mensaje recibido a todos los usuarios conectados
        for (Session sesion : conectados) {
            sesion.getBasicRemote().sendObject(mensaje);
        }
    }
}
