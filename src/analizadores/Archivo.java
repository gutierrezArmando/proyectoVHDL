package analizadores;

import java.io.*;

public class Archivo {

    public static String leerArchivo(String fileName)
    {
        String cadenaArchivo = "";
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try{
            archivo = new File(fileName);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while((linea=br.readLine())!=null)
                cadenaArchivo+=linea + "\n";
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try{
                if(fr!=null)
                    fr.close();
            }
            catch(Exception e2){
                e2.printStackTrace();
            }
        }
        return cadenaArchivo;
    }
}
