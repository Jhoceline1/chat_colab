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
import java.util.*;

@ServerEndpoint(value = "/chat_colab", encoders = {EncoderMensaje.class}, decoders = {DecoderMensaje.class})
public class MiChat {

    private static final List<Session> conectados = new ArrayList<>();
    private static final Set<Session> sesionesNotificadas = new HashSet<>();
    private static final Map<Session, String> nombresUsuarios = new HashMap<>();

    @OnOpen
    public void inicio(Session sesion) {
        conectados.add(sesion);
    }

    @OnClose
    public void salir(Session sesion) throws IOException, EncodeException {
        conectados.remove(sesion);
        
        String nombre = nombresUsuarios.getOrDefault(sesion, "Un usuario");

        Mensaje mensaje = new Mensaje();
        mensaje.setNombre("Sistema");
        mensaje.setMensaje(nombre + " ha salido del chat.");

        for (Session s : conectados) {
            s.getBasicRemote().sendObject(mensaje);
        }

        
        sesionesNotificadas.remove(sesion);
        nombresUsuarios.remove(sesion);
    }

    @OnMessage
    public void mensaje(Mensaje mensaje, Session sesion) throws IOException, EncodeException {
        
        if (!sesionesNotificadas.contains(sesion)) {
            
            nombresUsuarios.put(sesion, mensaje.getNombre());

            Mensaje mensajeBienvenida = new Mensaje();
            mensajeBienvenida.setNombre("Sistema");
            mensajeBienvenida.setMensaje(mensaje.getNombre() + " se ha unido al chat.");

            for (Session s : conectados) {
                s.getBasicRemote().sendObject(mensajeBienvenida);
            }

            sesionesNotificadas.add(sesion);
        }

        
        for (Session s : conectados) {
            s.getBasicRemote().sendObject(mensaje);
        }
    }
}

