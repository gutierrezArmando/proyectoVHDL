import analizadores.*;
import soporte.*;

import java.io.*;

public class Principal {
    public static void main(String[] args) {
        String resultado = "";
        Lexer lexer = new Lexer(Archivo.leerArchivo("archivosVHDL/prueba2.vhd"));
        Lexer lexer2 = new Lexer(Archivo.leerArchivo("archivosVHDL/prueba2.vhd"));
        Parser parser = new Parser(lexer);
        resultado = parser.programaCorrecto();
        if(resultado.equals("PROGRAMA"))
        {
            Convertidor convertidor = new Convertidor(parser.getArraySimbolos(), lexer2);
            convertidor.convertir();
            Archivo.escribirArchivo("Conversion.java", convertidor.getCodigo());
        }
        else
            System.out.println("Result: " + parser.programaCorrecto());

//        System.out.println(convertidor.getCodigo());
//
//        try {
//            Runtime runtime = Runtime.getRuntime();
//            String[] cmd = new String[3];
//            cmd[0] = "cmd.exe" ;
//            cmd[1] = "/C";
//            cmd[2] = "javac Conversion.java";
//            Process proc = runtime.exec(cmd);
//
//            InputStream is = proc.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            Runtime runtime = Runtime.getRuntime();
//            String[] cmd = new String[3];
//            cmd[0] = "cmd.exe" ;
//            cmd[1] = "/C";
//            cmd[2] = "java Conversion";
//            Process proc = runtime.exec(cmd);
//
//            InputStream is = proc.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}