import  soporte.*;

public class Principal {
    public static void main(String[] args) {

        PseudoParser parser = new PseudoParser( new Lexer(Archivo.leerArchivo("prueba.vhd")));

        System.out.println("Result: " + parser.programaCorrecto());
     }
}