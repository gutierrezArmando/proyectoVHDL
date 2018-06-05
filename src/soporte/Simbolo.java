package soporte;

public class Simbolo {
    private String nombre;
    private String clase;
    private String tipo_es;
    private String tipo_dato;

    public Simbolo() {
        this("","","","");
    }

    public Simbolo(String nombre, String clase) {
        this.setNombre(nombre);
        this.setClase(clase);
        this.setTipo_es("");
        this.setTipo_dato("");
    }

    public Simbolo(String nombre, String clase, String tipo_es, String tipo_dato) {
        this.setNombre(nombre);
        this.setClase(clase);
        this.setTipo_es(tipo_es);
        this.setTipo_dato(tipo_dato);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getTipo_es() {
        return tipo_es;
    }

    public void setTipo_es(String tipo_es) {
        this.tipo_es = tipo_es;
    }

    public String getTipo_dato() {
        return tipo_dato;
    }

    public void setTipo_dato(String tipo_dato) {
        this.tipo_dato = tipo_dato;
    }

    @Override
    public String toString(){
        if ( tipo_es.equals("") && tipo_dato.equals(""))
            return String.format("Nombre: %s\tClase: %s", nombre, clase);
        return String.format("Nombre: %s\tClase: %s\tTipo_es: %s\tTipo_dato: %s", nombre, clase, tipo_es, tipo_dato);
    }
}