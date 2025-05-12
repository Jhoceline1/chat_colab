package Objetos;

import java.util.List;

public class Mensaje {
    private String nombre;
    private String mensaje;
    private String tipo;
    private String estado;
    private List<String> lista;
    private String para;

    public Mensaje() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) { 
        this.mensaje = mensaje; 
    }

    public String getTipo() {
        return tipo; 
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo; 
    }

    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) { 
        this.estado = estado; 
    }

    public List<String> getLista() {
        return lista;
    }
    public void setLista(List<String> lista) { 
        this.lista = lista; 
    }
    
    public String getPara() {
        return para; 
    }
    
    public void setPara(String para) { 
        this.para = para;
    }
}
