import analizadores.*;
import soporte.Simbolo;

import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {
        PseudoParser2 parser = new PseudoParser2( new Lexer(Archivo.leerArchivo("prueba.vhd")));
        System.out.println("Result: " + parser.programaCorrecto());
        ArrayList<Simbolo> simbolos;
        simbolos = parser.getArraSimbolos();
        for(Simbolo simbolo: simbolos)
            if(!simbolo.getClase().equals("palabra reservada"))
                System.out.println(simbolo);
     }
}