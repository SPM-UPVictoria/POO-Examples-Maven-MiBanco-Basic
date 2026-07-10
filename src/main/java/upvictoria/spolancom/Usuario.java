package upvictoria.spolancom;

public class Usuario {
    private String nombre;
    private String identificacion;

    public Usuario(String n, String id) {
        this.nombre = n;
        this.identificacion = id;
    }

    public String get_nombre() {
        return this.nombre;
    }

    public void set_nombre(String n) {
        this.nombre = n;
    }

    @Override
    public String toString() {
        return "Usuario{nombre='" + this.nombre + "', identificacion:'" + this.identificacion + "'}";
    }
}
