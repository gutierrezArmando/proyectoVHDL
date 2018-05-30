package soporte;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PseudoParser {

    private ArrayList<String> tokens;
    private int indice;

    public PseudoParser(ArrayList<String> tokens) {
        this.tokens = tokens;
        indice = 0;
    }

    public PseudoParser() {
        this(null);
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }

    public String libreriaCorrecta() {
        if ( !match("LIBRARY", tokens.get(indice++)) )
            return "ERROR: Se esperaba: 'LIBRARY', recibido: " + tokens.get(--indice);
        if ( !match("NOMBREBIBLIOTECA", tokens.get(indice++)) )
            return "ERROR: Se esperaba: 'NOMBREBIBLIOTECA', recibido: " + tokens.get(--indice);
        try {
            return match("PUNTOYCOMA", tokens.get(indice++))?"LIBRERIA":"ERROR: Se esperaba: ';', recibido: " + tokens.get(indice);
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    public String paqueteCorrecto() {
        if ( !match("USE", tokens.get(indice++)) )
            return "ERROR: Se esperaba: 'USE', recibido: " + tokens.get(--indice);
        if ( !match("PAQUETE", tokens.get(indice++)) )
            return "ERROR: Se esperaba: 'NOMBREBIBLIOTECA', recibido: " + tokens.get(--indice);
        try {
            return match("PUNTOYCOMA", tokens.get(indice++))?"PAQUETE":"ERROR: Se esperaba: ';', recibido: " + tokens.get(indice);
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    public String pinCorrecto() {
        if ( !match("VARIABLE", tokens.get(indice++)) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(--indice);
        if ( !match("DOSPUNTOS", tokens.get(indice++)) )
            return "ERROR: Se esperaba: ':', recibido: " + tokens.get(--indice);
        if ( !match("TIPOPIN", tokens.get(indice++)) )
            return "ERROR: Se esperaba: 'TIPOPIN', recibido: " + tokens.get(--indice);
        try {
            return match("TIPODATO", tokens.get(indice++))?"PIN":"ERROR: Se esperaba: 'TIPODATO', recibido: " + tokens.get(indice);
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    private boolean match(String expresion, String valor) {
        Pattern patron = Pattern.compile(expresion);
        Matcher m = patron.matcher(valor);
        return m.find();
    }

}
