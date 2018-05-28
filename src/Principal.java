import  soporte.*;

public class Principal {
    public static void main(String[] args) {

        String datos = Archivo.leerArchivo("prueba.vhd");
        Lexer lexer = new Lexer(datos);
        Lexer.Token token;
        while ((token = lexer.nextToken())!=null)
            System.out.println(token);

    }
}