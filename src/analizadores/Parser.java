package analizadores;

import soporte.Simbolo;
import soporte.TablaSimbolos;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private ArrayList<Lexer.Token> tokens;
    private Lexer.Token token;
    private int indice;
    private String nombreEntidad;
    private String nombreArquitectura;
    private String msg;
    private TablaSimbolos tabla;

    public Parser() {
        this(null);
    }

    /** Constructor */
    public Parser(Lexer lexer) {
        tokens = new ArrayList<Lexer.Token>();
        indice = 0;
        while ((token = lexer.nextToken())!=null)
            tokens.add(token);
        nombreEntidad = "entidad";
        tabla = new TablaSimbolos();
    }

    /**Devuelve en forma de cadena todos los token*/
    public String getStrTokens(){
        String cadena="";
        for( Lexer.Token strToken: tokens)
            cadena += strToken.type.toString() + "\n";
        return cadena;
    }

    /** Verifica que el enunciado para librerias este correcto*/
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
        Simbolo simbolo = new Simbolo();
        if ( !match("VARIABLE", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(--indice).data;
        if (tabla.exist(tokens.get(indice-1).data.toUpperCase()))
            return "ERROR: pin '"+tokens.get(--indice).data+"' ya existente";
        simbolo.setNombre(tokens.get(indice-1).data);
        simbolo.setClase("PIN");
        if ( !match("DOSPUNTOS", nextStrTokenType()) )
            return "ERROR: Se esperaba: ':', recibido: " + tokens.get(--indice).data;
        if ( !match("IOTYPE", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'IOTYPE', recibido: " + tokens.get(--indice).data;
        simbolo.setTipo_es(tokens.get(indice-1).data.toUpperCase());
        try {
            if(!match("TIPODATO", nextStrTokenType()))
                return "ERROR: Se esperaba: 'TIPODATO', recibido: " + tokens.get(--indice).data;
            simbolo.setTipo_dato(tokens.get(indice-1).data.toUpperCase());
        } catch (Exception e) {
            return "No Finalizo correctamente";
        }
        tabla.put(simbolo.getNombre(), simbolo);
        return "PIN";
    }

    private String puertosCorrecto() {
        if ( !match("PORT", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'PORT', recibido: " + tokens.get(--indice).data;
        if ( !match("PARENTESISIZQ", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'PARENTESISIZQ', recibido: " + tokens.get(--indice).data;
        do {
            if (!match("PIN",(msg = pinCorrecto())))
                return msg;
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

        if(tabla.exist(tokens.get(indice).data))
            return "ERROR: " + tokens.get(indice).data + " es " + tabla.get(tokens.get(indice).data).getClase();
        tabla.addEntidad(tokens.get(indice).data);

        nombreEntidad = tokens.get(indice++).data;
        if ( !match("IS", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'IS', recibido: " + tokens.get(--indice).data;
        if ( !match("PUERTOS", (msg = puertosCorrecto())))
            return  msg;
        if ( !match("END", nextStrTokenType()) )
            return "ERROR: Se esperaba: 'END', recibido: " + tokens.get(--indice).data;
        if ( !match("VARIABLE", tokens.get(indice).type.toString()) )
            return "ERROR: Se esperaba: 'VARIABLE', recibido: " + tokens.get(indice).data;

        if(!tabla.exist(tokens.get(indice).data))
            return "ERROR: No se encontro la entidad " + tokens.get(indice).data;

        if(!tabla.get(tokens.get(indice++).data).getClase().equals("entity"))
            return "ERROR: " + tokens.get(--indice).data + " no es entidad";
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
        do {
            if( !match("VARIABLE", nextStrTokenType()))
                return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(--indice).data;
            if( !match("IGUAL", nextStrTokenType()))
                return "ERROR: Se esperaba: IGUAL, recibido: " + tokens.get(--indice).data;
            if( !match("PINVAL", nextStrTokenType()))
                return "ERROR: Se esperaba: PINVAL, recibido: " + tokens.get(--indice).data;
        }while (match("OPLOGICO", nextStrTokenType()));
        indice--;
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
        do{
            if (!match("ENUNCIADO", (msg = enunciadoCorrecto())))
                return msg;
        }while (!match("ELSE", tokens.get(indice).type.toString()));

        if( !match("ELSE", nextStrTokenType()))
            return "ERROR: Se esperaba: ELSE, recibido: " + tokens.get(--indice).data;
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
            if(!tabla.exist(tokens.get(indice-1).data))
                return "Error: PIN '" + tokens.get(--indice).data + "' no declarado";
            if( !tabla.get(tokens.get(indice-1).data).getClase().equals("PIN"))
                return "Error: " + tokens.get(indice-1).data + " definido como: " + tabla.get(tokens.get(indice-1).data).getClase();
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
                if(!tabla.exist(tokens.get(indice).data))
                    return "Error: PIN '" + tokens.get(indice).data + "' no declarado";
                if( !tabla.get(tokens.get(indice).data).getClase().equals("PIN"))
                    return "Error: " + tokens.get(indice).data + " definido como: " + tabla.get(tokens.get(indice-1).data).getClase();
                if(!tabla.get(tokens.get(indice).data).getTipo_es().equals("OUT"))
                    return "Error: No puede asignarse un valor  al PIN '"+tokens.get(indice).data+"' tipo 'IN'";
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

    private String arquitecturaCorrecto() {
        if( !match("ARCHITECTURE", nextStrTokenType()))
            return "ERROR: Se esperaba: ARCHITECTURE, recibido: " + tokens.get(--indice).data;
        if( !match("VARIABLE", tokens.get(indice).type.toString()))
            return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(indice).data;

        if (tabla.exist(tokens.get(indice).data))
            return "ERROR: Duplicado\n" + tabla.get(tokens.get(indice).data);

        tabla.addArquitectura(tokens.get(indice++).data);

        if( !match("OF", nextStrTokenType()))
            return "ERROR: Se esperaba: OF, recibido: " + tokens.get(--indice).data;
        if( !match("VARIABLE", tokens.get(indice).type.toString()))
            return "ERROR: Se esperaba: VARIABLE, recibido: " + tokens.get(indice).data;

        if(!tabla.exist(tokens.get(indice).data))
            return "Error: entity '" + tokens.get(indice).data + "' no fue localizado";
        if(!tabla.get(tokens.get(indice++).data).getClase().equals("entity"))
            return "Error: " + tabla.get(tokens.get(--indice).data);

        if( !match("IS", nextStrTokenType()))
            return "ERROR: Se esperaba: IS, recibido: " + tokens.get(--indice).data;
        if( !match("BEGIN", nextStrTokenType()))
            return "ERROR: Se esperaba: BEGIN, recibido: " + tokens.get(--indice).data;
        if(!match("PROCESO",(msg = procesoCorrecto())))
            return msg;
        if( !match("END", nextStrTokenType()))
            return "ERROR: Se esperaba: END, recibido: " + tokens.get(--indice).data;
        if(!tabla.exist(tokens.get(indice).data))
            return "Clase architect '" + tokens.get(indice).data + "' no iniciado";
        if(!tabla.get(tokens.get(indice++).data).getClase().equals("architecture"))
            return "'" + tokens.get(--indice).data + "' no es de clase architecture";
        try {
            if( !match("PUNTOYCOMA", nextStrTokenType()))
                return "ERROR: Se esperaba: ';', recibido: " + tokens.get(--indice).data;
        }catch (Exception e) {
            return "Fin de cadena incorrecto";
        }
        return "ARQUITECTURA";
    }

    public String programaCorrecto() {
        if(!match("LIBRERIA", (msg = libreriaCorrecta())))
            return msg;
        if(!match("PAQUETE", (msg = paqueteCorrecto())))
            return msg;
        if(!match("ENTIDAD", (msg = entidadCorrecta())))
            return msg;
        if(!match("ARQUITECTURA", (msg = arquitecturaCorrecto())))
            return msg;
        return "PROGRAMA";
    }

    public ArrayList<Simbolo> getArraySimbolos() {
        return tabla.getArraSimbolos();
    }

    private String nextStrTokenType() {
        return tokens.get(indice++).type.toString();
    }

    private boolean match(String expresion, String valor) {
        Pattern patron = Pattern.compile(expresion);
        Matcher m = patron.matcher(valor);
        return m.find();
    }
}