package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public abstract class NodoOperando extends NodoExpresion {
    protected Token token;
    public NodoOperando(Token token){
        this.token = token;
    }
    public Token getToken() { return token; }
    public abstract AbstractType check() throws SemanticException;
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        return toReturn + token.getLexeme() + "\n";
    }
}
