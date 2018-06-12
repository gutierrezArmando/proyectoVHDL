package soporte;

import analizadores.Lexer;

import java.util.ArrayList;

public class Convertidor {

    private ArrayList<Simbolo> simbolos;
    private Lexer lexer;
    private String codigoJava;
    private int tabuladores;
    private boolean finDocumento;
    private boolean variableDeParametro;

    public Convertidor(ArrayList<Simbolo> tablaSimbolos, Lexer lexer) {
        this.simbolos = tablaSimbolos;
        this.lexer = lexer;
        codigoJava = "public class Conversion {\n" +
        "\tpublic static void main (String args[]) {\n";
        tabuladores = 2;
        finDocumento=false;
        variableDeParametro=false;
    }

    public void convertir() {
        addVariables();
        addCodigo();
    }

    private void addVariables(){
        for( Simbolo simbolo: simbolos)
            if (simbolo.getClase().equals("PIN"))
                codigoJava += String.format("%sint %s = 0;\n", getTabulador(), simbolo.getNombre());
    }

    private void addCodigo() {
        Lexer.Token var;
        while (!(var = lexer.nextToken()).type.toString().equals("PROCESS"));
        while (!(var = lexer.nextToken()).type.toString().equals("BEGIN"));
        while (!finDocumento&&(var = lexer.nextToken())!= null) {
            addNewTokenData(var);
        }

    }

    private void addNewTokenData(Lexer.Token token) {
        switch (token.type) {
            case IF: codigoJava+= getTabulador() + "if"; break;
            case PARENTESISIZQ: {
                codigoJava+= "( ";
                variableDeParametro = true;
            } break;
            case VARIABLE:{
                if (variableDeParametro)
                    codigoJava+= token.data;
                else {
                    codigoJava+=getTabulador() + token.data;
                }
            } break;
            case IGUAL: codigoJava+= " == "; break;
            case PINVAL: codigoJava+= token.data.equals("'0'")? "0":"1"; break;
            case OPLOGICO: codigoJava += token.data.equals("and")?" && ":" || "; break;
            case PARENTESISDER: {
                tabuladores++;
                codigoJava+= ") {\n";
                variableDeParametro = false;
            }break;
            case ASIGNA: codigoJava+= " = "; break;
            case PUNTOYCOMA: codigoJava += ";\n";break;
            case END: {
                switch (lexer.nextToken().type)
                {
                    case IF:{
                        tabuladores--;
                        codigoJava+= getTabulador() +  "}\n";
                        lexer.nextToken();
                    } break;
                    case PROCESS:{
                        tabuladores--;
                        codigoJava+=getTabulador() + "System.out.println(\"Hola desde el convertidor\");\n";
                        codigoJava+=getTabulador() + "}\n}";
                        finDocumento=true;
                    }break;
                }
            }break;
            case ELSE: {
                tabuladores--;
                codigoJava+= getTabulador() + "}\n"+ getTabulador()+"else {\n";
                tabuladores++;
            }
        }
    }

    private String getTabulador() {
        String strTab="";
        for( int i = 0;i<tabuladores;i++)
            strTab += "\t";
        return strTab;
    }

    public String getCodigo() {
        return codigoJava;
    }

}