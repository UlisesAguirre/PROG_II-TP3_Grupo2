package entidades;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String correo;
    private String contrasena;

    public Usuario() {
    }
    public Usuario(int id,String nombre,String correo,String contrasena) {
        this.id=id;
        this.nombre=nombre;
        this.correo=correo;
        this.contrasena=contrasena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
