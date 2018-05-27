
public class Principal {
    public static void main(String[] args) {
        Persona p = new Persona("Armando", 34);
        System.out.println(p.toString());
    }
}

class Persona {
    private String nombre;
    private int edad;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public Persona() {
        this("", 0);
    }

    @Override
    public String toString () {
        return String.format("Nombre: %s\nEdad: %d", this.nombre, this.edad);
    }
}