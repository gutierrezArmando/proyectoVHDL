package  soporte;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    ArrayList<Token> tokens = new ArrayList<Token>();
    int pos=0;

    public enum TokenType {
        //No se puede usar el guion en el nombre
        NUMERO("[0-9]+"),
        PINVAL("'[0 1]'"),
        TIPODATO("std_logic|STD_LOGIC|bit|BIT"),
        LIBRARY("library|LIBRARY"),
        PAQUETE("ieee.std_logic_1164.all|IEEE.STD_LOGIC_1164.ALL"),
        NOMBREBIBLIOTECA("ieee|IEEE"),
        USE("use|USE"),
        ENTITY("entity|ENTITY"),
        IS("is|IS"),
        PORT("port|PORT"),
        IOTYPE("in|IN|out|OUT"),
        END("end|END"),
        ARCHITECTURE("architecture|ARCHITECTURE"),
        OF("of|OF"),
        BEGIN("begin|BEGIN"),
        PROCESS("process|PROCESS"),
        IF("if|IF"),
        THEN("then|THEN"),
        ELSE("else|ELSE"),
        OPLOGICO("and|or|xor|AND|OR|XOR"),
        IGUAL("="),
        ASIGNA("=>|<="),
        PUNTOYCOMA(";"),
        DOSPUNTOS(":"),
        COMA(","),
        PARENTESISIZQ("\\("),
        PARENTESISDER("\\)"),
        ESPACIOS("[ \t\f\r\n]+"),
        VARIABLE("[a-zA-Z][a-zA=Z0-9_]*"),
        ERROR(".+");

//        NUMERO("-?[0-9]+(\\.([0-9]+))?"),
//        CADENA("\".*\""),
//        OPARITMETICO("[*|/|+|-]"),
//        OPRELACIONAL("<|>|==|<=|>=|!="),
//        IGUAL("="),
//        INICIOPROGRAMA("inicio-programa"),
//        FINPROGRAMA("fin-programa"),
//        SI("si"),
//        ENTONCES("entonces"),
//        FINSI("fin-si"),
//        MIENTRAS("mientras"),
//        FINMIENTRAS("fin-mientras"),
//        LEER("leer"),
//        ESCRIBIR("escribir"),
//        COMA(","),
//        PARENTESISIZQ("\\("),
//        PARENTESISDER("\\)"),
//        ESPACIOS("[ \t\f\r\n]+"),
//        VARIABLE("[a-zA-Z][a-zA=Z0-0]*"),
//        ERROR(".+");

        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }

    }
    public class Token{
        public TokenType type;
        public String data;


        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public String toString() {
            return String.format("(%s	\"%s\")",type.name(),data);
        }

    }


    public Lexer(String input){

        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for(TokenType tokenType : TokenType.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));
        Matcher matcher = tokenPatterns.matcher(input);

        while(matcher.find()) {
            for(TokenType t : TokenType.values()) {
                if(matcher.group(TokenType.ESPACIOS.name())!=null)
                    continue;
                else if (matcher.group(t.name())!=null){
                    tokens.add(new Token(t, matcher.group(t.name())));
                    break;
                }
            }
        }

//		nextToken();
    }

    public Token nextToken() {
        if(pos < tokens.size()) {
            return tokens.get(pos++);
        }
        else
            return null;
    }

}