import  soporte.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        String texto = teclado.nextLine();
        Lexer lexer = new Lexer(texto);
//        PseudoParser parser = new PseudoParser(Archivo.leerArchivo("prueba.vhd"));
        PseudoParser parser = new PseudoParser(lexer);
//        Lexer.Token t;
//        while ((t=lexer.nextToken())!=null)
//            System.out.println(t);
//        System.out.println(parser.getStrTokens());
        System.out.println("Result: " + parser.procesoCorrecto());
     }
}