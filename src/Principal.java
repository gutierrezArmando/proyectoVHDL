import analizadores.*;
import soporte.Simbolo;
import soporte.TablaSimbolos;

public class Principal {
    public static void main(String[] args) {
//        PseudoParser parser = new PseudoParser( new Lexer(Archivo.leerArchivo("prueba.vhd")));
//        System.out.println("Result: " + parser.programaCorrecto());
        TablaSimbolos tablaSimbolos = new TablaSimbolos();
        tablaSimbolos.put("library", new Simbolo("library"));

        System.out.println(tablaSimbolos.exist("use"));
     }
}