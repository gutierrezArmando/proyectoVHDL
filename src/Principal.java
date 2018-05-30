import  soporte.*;

import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {

        String datos = Archivo.leerArchivo("prueba.vhd");
        ArrayList<String> listaTokens = new ArrayList<String>();
        Lexer lexer = new Lexer("a : in std_logic");
//        Lexer lexer = new Lexer(datos);

        Lexer.Token token;
        while ((token = lexer.nextToken())!=null) {
            listaTokens.add(token.type.toString());
            System.out.println(token);
        }
        PseudoParser parser = new PseudoParser(listaTokens);

        System.out.println("Result: " + parser.pinCorrecto());
     }
}