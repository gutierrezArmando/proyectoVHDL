package soporte;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PseudoParser {

    private ArrayList<Lexer.Token> tokens;
    private Lexer.Token token;
    private int indice;
    private String nombreEntidad;
    private String msg;

    public PseudoParser() {
        this(null);
    }

    public PseudoParser(Lexer lexer) {
        tokens = new ArrayList<Lexer.Token>();
        while ((token = lexer.nextToken())!=null)
            tokens.add(token);
        indice = 0;
    }

    public ArrayList<Lexer.Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Lexer.Token> tokens) {
        this.tokens = tokens;
    }

    public String getStrTokens(){
        String cadena="";
        for( Lexer.Token strToken: tokens)
            cadena += strToken.type.toString() + "\n";
        return cadena;
    }

    private String libreriaCorrecta() {
        if ( !match("LIBRARY", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'LIBRARY', recibido: " + tokens.get(--indice).data;
        if ( !match("NOMBREBIBLIOTECA", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'NOMBREBIBLIOTECA', recibido: " + tokens.get(--indice).data;
        try {
            return match("PUNTOYCOMA", tokens.get(indice++).type.toString())?"LIBRERIA":"ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    private String paqueteCorrecto() {
        if ( !match("USE", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'USE', recibido: " + tokens.get(--indice).data;
        if ( !match("PAQUETE", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'NOMBREBIBLIOTECA', recibido: " + tokens.get(--indice).data;
        try {
            return match("PUNTOYCOMA", tokens.get(indice++).type.toString())?"PAQUETE":"ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    private String pinCorrecto() {
        if ( !match("VARIABLE", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(--indice).data;
        if ( !match("DOSPUNTOS", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: ':', recibido: " + tokens.get(--indice).data;
        if ( !match("IOTYPE", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'IOTYPE', recibido: " + tokens.get(--indice).data;
        try {
            return match("TIPODATO", tokens.get(indice++).type.toString())?"PIN":"ERROR: Se esperaba: 'TIPODATO', recibido: " + tokens.get(--indice).data;
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    private String puertosCorrecto() {
//        String msg;
        if ( !match("PORT", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'PORT', recibido: " + tokens.get(--indice).data;
        if ( !match("PARENTESISIZQ", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'PARENTESISIZQ', recibido: " + tokens.get(--indice).data;
        do {
            if (!match("PIN",(msg = pinCorrecto()))) {
                return msg;
            }
        }while (match("PUNTOYCOMA", tokens.get(indice++).type.toString()));
        indice--;
        if ( !match("PARENTESISDER", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'PARENTESISDER', recibido: " + tokens.get(--indice).data;
        try {
            if ( !match("PUNTOYCOMA", tokens.get(indice++).type.toString()) )
                return "ERROR: Se esperaba: 'PUNTOYCOMA', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "No Finalizo correctamente";
        }
        return "PUERTOS";
    }

    private String entidadCorrecta(){

        if ( !match("ENTITY", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'ENTITY', recibido: " + tokens.get(--indice).data;
        if ( !match("VARIABLE", tokens.get(indice).type.toString()) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(indice).data;
        nombreEntidad = tokens.get(indice++).data;
        if ( !match("IS", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'IS', recibido: " + tokens.get(--indice).data;
        if ( !match("PUERTOS", (msg = puertosCorrecto())))
            return  msg;
        if ( !match("END", tokens.get(indice++).type.toString()) )
            return "ERROR: Se esperaba: 'END', recibido: " + tokens.get(--indice).data;
        if ( !match("VARIABLE", tokens.get(indice).type.toString()) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(indice).data;
        if( !match(nombreEntidad, tokens.get(indice++).data))
            return "ERROR: Se esperaba: " + nombreEntidad + ", recibido: " + tokens.get(--indice).data;
        try {
            if ( !match("PUNTOYCOMA", tokens.get(indice++).type.toString()) )
                return "ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        } catch (Exception e) {
            return "No finalizo correctamente";
        }
        return "ENTIDAD";
    }

    private String comparacionCorrecta(){
        if( !match("PARENTESISIZQ", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: '(', recibido: " + tokens.get(--indice).data;
        if( !match("VARIABLE", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(--indice).data;
        if( !match("IGUAL", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: IGUAL, recibido: " + tokens.get(--indice).data;
        if( !match("PINVAL", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: PINVAL, recibido: " + tokens.get(--indice).data;
        try {
            if( !match("PARENTESISDER", tokens.get(indice++).type.toString()))
                return "ERROR: Se esperaba: ')', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "No finalizo correctamente";
        }
        return  "COMPARACION";
    }

    private String asignacionCorrecta() {
        if( !match("VARIABLE", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(--indice).data;
        if( !match("ASIGNA", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: <=, recibido: " + tokens.get(--indice).data;
        if( !match("PINVAL", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: PINVAL, recibido: " + tokens.get(--indice).data;
        try {
            if( !match("PUNTOYCOMA", tokens.get(indice++).type.toString()))
                return "ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "Error de Finalizacion en cadena";
        }
        return "ASIGNACION";
    }

    public String condicionSICorrecto(){
        if( !match("IF", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: IF, recibido: " + tokens.get(--indice).data;
        if(!match("COMPARACION", (msg=comparacionCorrecta())))
            return msg;
        if( !match("THEN", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: THEN, recibido: " + tokens.get(--indice).data;
        if(!match("ASIGNACION", (msg=asignacionCorrecta())))
            return  msg;
        if( !match("ELSE", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: ELSE, recibido: " + tokens.get(--indice).data;
        if(!match("ASIGNACION", (msg=asignacionCorrecta())))
            return  msg;
        if( !match("END", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: END, recibido: " + tokens.get(--indice).data;
        if( !match("IF", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: IF, recibido: " + tokens.get(--indice).data;
        if( !match("PUNTOYCOMA", tokens.get(indice++).type.toString()))
            return "ERROR: Se esperaba: ;, recibido: " + tokens.get(--indice).data;
        return "SI";
    }

    private boolean match(String expresion, String valor) {
        Pattern patron = Pattern.compile(expresion);
        Matcher m = patron.matcher(valor);
        System.out.println("Cheking: " + expresion + " and " + valor);
        return m.find();
    }
}