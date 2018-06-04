package soporte;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PseudoParser {

    private ArrayList<Lexer.Token> tokens;
    private Lexer.Token token;
    private int indice;
    private String nombreEntidad;
    private String nombreArquitectura;
    private String msg;

    public PseudoParser() {
        this(null);
    }

    public PseudoParser(Lexer lexer) {
        tokens = new ArrayList<Lexer.Token>();
        while ((token = lexer.nextToken())!=null)
            tokens.add(token);
        indice = 0;
        nombreEntidad = "entidad";
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
        if ( !match("LIBRARY", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'LIBRARY', recibido: " + tokens.get(--indice).data;
        if ( !match("NOMBREBIBLIOTECA", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'NOMBREBIBLIOTECA', recibido: " + tokens.get(--indice).data;
        try {
            return match("PUNTOYCOMA", nextStrTokenType())?"LIBRERIA":"ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    private String paqueteCorrecto() {
        if ( !match("USE", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'USE', recibido: " + tokens.get(--indice).data;
        if ( !match("PAQUETE", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'NOMBREBIBLIOTECA', recibido: " + tokens.get(--indice).data;
        try {
            return match("PUNTOYCOMA", nextStrTokenType())?"PAQUETE":"ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    private String pinCorrecto() {
        if ( !match("VARIABLE", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(--indice).data;
        if ( !match("DOSPUNTOS", nextStrTokenType()) )
            return "ERROR: Se esperaba: ':', recibido: " + tokens.get(--indice).data;
        if ( !match("IOTYPE", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'IOTYPE', recibido: " + tokens.get(--indice).data;
        try {
            return match("TIPODATO", nextStrTokenType())?"PIN":"ERROR: Se esperaba: 'TIPODATO', recibido: " + tokens.get(--indice).data;
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
    }

    private String puertosCorrecto() {
//        String msg;
        if ( !match("PORT", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'PORT', recibido: " + tokens.get(--indice).data;
        if ( !match("PARENTESISIZQ", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'PARENTESISIZQ', recibido: " + tokens.get(--indice).data;
        do {
            if (!match("PIN",(msg = pinCorrecto()))) {
                return msg;
            }
        }while (match("PUNTOYCOMA", nextStrTokenType()));
        indice--;
        if ( !match("PARENTESISDER", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'PARENTESISDER', recibido: " + tokens.get(--indice).data;
        try {
            if ( !match("PUNTOYCOMA", nextStrTokenType()) )
                return "ERROR: Se esperaba: 'PUNTOYCOMA', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "No Finalizo correctamente";
        }
        return "PUERTOS";
    }

    private String entidadCorrecta(){

        if ( !match("ENTITY", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'ENTITY', recibido: " + tokens.get(--indice).data;
        if ( !match("VARIABLE", tokens.get(indice).type.toString()) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(indice).data;
        nombreEntidad = tokens.get(indice++).data;
        if ( !match("IS", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'IS', recibido: " + tokens.get(--indice).data;
        if ( !match("PUERTOS", (msg = puertosCorrecto())))
            return  msg;
        if ( !match("END", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'END', recibido: " + tokens.get(--indice).data;
        if ( !match("VARIABLE", tokens.get(indice).type.toString()) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(indice).data;
        if( !match(nombreEntidad, tokens.get(indice++).data))
            return "ERROR: Se esperaba: " + nombreEntidad + ", recibido: " + tokens.get(--indice).data;
        try {
            if ( !match("PUNTOYCOMA", nextStrTokenType()) )
                return "ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        } catch (Exception e) {
            return "No finalizo correctamente";
        }
        return "ENTIDAD";
    }

    private String comparacionCorrecta(){
        if( !match("PARENTESISIZQ", nextStrTokenType()))
            return "ERROR: Se esperaba: '(', recibido: " + tokens.get(--indice).data;
        if( !match("VARIABLE", nextStrTokenType()))
            return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(--indice).data;
        if( !match("IGUAL", nextStrTokenType()))
            return "ERROR: Se esperaba: IGUAL, recibido: " + tokens.get(--indice).data;
        if( !match("PINVAL", nextStrTokenType()))
            return "ERROR: Se esperaba: PINVAL, recibido: " + tokens.get(--indice).data;
        try {
            if( !match("PARENTESISDER", nextStrTokenType()))
                return "ERROR: Se esperaba: ')', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "No finalizo correctamente";
        }
        return  "COMPARACION";
    }

    private String asignacionCorrecta() {
        if( !match("VARIABLE", nextStrTokenType()))
            return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(--indice).data;
        if( !match("ASIGNA", nextStrTokenType()))
            return "ERROR: Se esperaba: <=, recibido: " + tokens.get(--indice).data;
        if( !match("PINVAL", nextStrTokenType()))
            return "ERROR: Se esperaba: PINVAL, recibido: " + tokens.get(--indice).data;
        try {
            if( !match("PUNTOYCOMA", nextStrTokenType()))
                return "ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "Error de Finalizacion en cadena";
        }
        return "ASIGNACION";
    }

    private String condicionSICorrecto(){
        if( !match("IF", nextStrTokenType()))
            return "ERROR: Se esperaba: IF, recibido: " + tokens.get(--indice).data;
        if(!match("COMPARACION", (msg=comparacionCorrecta())))
            return msg;
        if( !match("THEN", nextStrTokenType()))
            return "ERROR: Se esperaba: THEN, recibido: " + tokens.get(--indice).data;
//        if(!match("ASIGNACION", (msg=asignacionCorrecta())))
//            return  msg;
        if (!match("ENUNCIADO", (msg = enunciadoCorrecto())))
            return msg;

        if( !match("ELSE", nextStrTokenType()))
            return "ERROR: Se esperaba: ELSE, recibido: " + tokens.get(--indice).data;
//        if(!match("ASIGNACION", (msg=asignacionCorrecta())))
//            return  msg;
        if (!match("ENUNCIADO", (msg = enunciadoCorrecto())))
            return msg;

        if( !match("END", nextStrTokenType()))
            return "ERROR: Se esperaba: END, recibido: " + tokens.get(--indice).data;
        if( !match("IF", nextStrTokenType()))
            return "ERROR: Se esperaba: IF, recibido: " + tokens.get(--indice).data;
        if( !match("PUNTOYCOMA", nextStrTokenType()))
            return "ERROR: Se esperaba: ;, recibido: " + tokens.get(--indice).data;
        return "SI";
    }

    private String parametrosCorrecto() {
        if( !match("PARENTESISIZQ", nextStrTokenType()))
            return "ERROR: Se esperaba: '(', recibido: " + tokens.get(--indice).data;
        do {
            if (!match("VARIABLE",nextStrTokenType()))
                return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(--indice).data;
        }while (match("COMA", nextStrTokenType()));
        indice--;
        try {
            if( !match("PARENTESISDER", nextStrTokenType()))
                return "ERROR: Se esperaba: ')', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "Fin de cadena incorrecto";
        }
        return "PARAMETROS";
    }

    private String enunciadoCorrecto() {
        switch (tokens.get(indice).type.toString())
        {
            case "IF" :
            {
                if(!match("SI", (msg = condicionSICorrecto())))
                    return msg;
            }break;
            case "VARIABLE" :
            {
                if(!match("ASIGNACION", (msg = asignacionCorrecta())))
                    return msg;
            }break;
        }
        return "ENUNCIADO";
    }

    private String procesoCorrecto() {
        if( !match("PROCESS", nextStrTokenType()))
            return "ERROR: Se esperaba: 'PROCESS', recibido: " + tokens.get(--indice).data;
        if(!match("PARAMETROS",(msg = parametrosCorrecto())))
            return msg;
        if( !match("BEGIN", nextStrTokenType()))
            return "ERROR: Se esperaba: 'BEGIN', recibido: " + tokens.get(--indice).data;
        if(!match("ENUNCIADO",(msg = enunciadoCorrecto())))
            return msg;
        if( !match("END", nextStrTokenType()))
            return "ERROR: Se esperaba: END, recibido: " + tokens.get(--indice).data;
        if( !match("PROCESS", nextStrTokenType()))
            return "ERROR: Se esperaba: PROCESS, recibido: " + tokens.get(--indice).data;
        try {
            if( !match("PUNTOYCOMA", nextStrTokenType()))
                return "ERROR: Se esperaba: ;, recibido: " + tokens.get(--indice).data;
        }catch (Exception e){
            return "Fin de sentencia incorrecto";
        }
        return "PROCESO";
    }

    public String arquitecturaCorrecto() {
        if( !match("ARCHITECTURE", nextStrTokenType()))
            return "ERROR: Se esperaba: ARCHITECTURE, recibido: " + tokens.get(--indice).data;
        if( !match("VARIABLE", tokens.get(indice).type.toString()))
            return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(indice).data;
        nombreArquitectura = tokens.get(indice++).data;
        if( !match("OF", nextStrTokenType()))
            return "ERROR: Se esperaba: OF, recibido: " + tokens.get(--indice).data;
        if( !match("VARIABLE", tokens.get(indice).type.toString()))
            return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(indice).data;
        if(!match(nombreEntidad, tokens.get(indice++).data))
            return "Entidad no correspondiente, se esperaba: " + nombreEntidad + " recibido: " + tokens.get(--indice).data;
        if( !match("IS", nextStrTokenType()))
            return "ERROR: Se esperaba: IS, recibido: " + tokens.get(--indice).data;
        if( !match("BEGIN", nextStrTokenType()))
            return "ERROR: Se esperaba: BEGIN, recibido: " + tokens.get(--indice).data;
        if(!match("PROCESO",(msg = procesoCorrecto())))
            return msg;
        if( !match("END", nextStrTokenType()))
            return "ERROR: Se esperaba: END, recibido: " + tokens.get(--indice).data;
        if(!match(nombreArquitectura, tokens.get(indice++).data))
            return "Se esperaba: " + nombreArquitectura + ", Recibido: " + tokens.get(--indice).data;
        try {
            if( !match("PUNTOYCOMA", nextStrTokenType()))
                return "ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "Fin de cadena incorrecto";
        }
        return "ARQUITECTURA";
    }

    private String nextStrTokenType() {
        return tokens.get(indice++).type.toString();
    }



    private boolean match(String expresion, String valor) {
        Pattern patron = Pattern.compile(expresion);
        Matcher m = patron.matcher(valor);
        System.out.println("Cheking: " + expresion + " and " + valor);
        return m.find();
    }
}