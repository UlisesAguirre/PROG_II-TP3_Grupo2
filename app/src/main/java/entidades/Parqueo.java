package entidades;

public class Parqueo {
    private int id;
    private String matricula;
    private int tiempo;
    private int usuarioId;

    public Parqueo() {
    }

    public Parqueo(int id, String matricula, int tiempo, int usuarioId) {
        this.id = id;
        this.matricula = matricula;
        this.tiempo = tiempo;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}