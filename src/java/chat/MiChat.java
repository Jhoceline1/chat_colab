package chat;

import Objetos.DecoderMensaje;
import Objetos.EncoderMensaje;
import Objetos.Mensaje;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/chat_colab", encoders = {EncoderMensaje.class}, decoders = {DecoderMensaje.class})
public class MiChat {

    private static final Map<Session, String> usuarios = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {}

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        String nombre = usuarios.get(session);
        usuarios.remove(session);

        if (nombre != null) {
            Mensaje notificacion = new Mensaje();
            notificacion.setTipo("sistema");
            notificacion.setNombre(nombre);
            notificacion.setEstado("salida");
            enviarATodos(notificacion);
            enviarListaUsuarios();
        }
    }

    @OnMessage
    public void onMessage(Mensaje mensaje, Session session) throws IOException, EncodeException {
        if (!usuarios.containsKey(session)) {
            usuarios.put(session, mensaje.getNombre());

            Mensaje notificacion = new Mensaje();
            notificacion.setTipo("sistema");
            notificacion.setNombre(mensaje.getNombre());
            notificacion.setEstado("entrada");
            enviarATodos(notificacion);
            enviarListaUsuarios();
        }
        if ("privado".equals(mensaje.getTipo())) {
            Mensaje copia = new Mensaje();
            copia.setTipo("privado");
            copia.setNombre(mensaje.getNombre());
            copia.setMensaje(mensaje.getMensaje());
            copia.setPara(mensaje.getPara());

            for (Map.Entry<Session, String> entry : usuarios.entrySet()) {
                if (entry.getValue().equals(copia.getPara()) || entry.getValue().equals(copia.getNombre())) {
                    entry.getKey().getBasicRemote().sendObject(copia);
                }
            }
            return;
        }
        Mensaje publico = new Mensaje();
        publico.setTipo("mensaje");
        publico.setNombre(mensaje.getNombre());
        publico.setMensaje(mensaje.getMensaje());
        publico.setPara("todos");

        enviarATodos(publico);

    }

    private void enviarATodos(Mensaje mensaje) throws IOException, EncodeException {
        for (Session sesion : usuarios.keySet()) {
            sesion.getBasicRemote().sendObject(mensaje);
        }
    }

    private void enviarListaUsuarios() throws IOException, EncodeException {
        Mensaje lista = new Mensaje();
        lista.setTipo("usuarios");
        lista.setLista(new ArrayList<>(usuarios.values()));
        enviarATodos(lista);
    }
}
