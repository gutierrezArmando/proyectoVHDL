package soporte;

import java.util.ArrayList;

public class Pila {

    ArrayList<Lexer.Token> tokens;
    int indice;

    public Pila() {
        tokens = new ArrayList<Lexer.Token>();
        indice = 0;
    }

    public void push(Lexer.Token token) {
        tokens.add(token);
        indice++;
    }

    public Lexer.Token pop() {
        Lexer.Token token = tokens.get(--indice);
        tokens.remove(indice);
        return token;
    }

}
