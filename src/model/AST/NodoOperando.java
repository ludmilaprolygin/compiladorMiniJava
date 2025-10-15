package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public abstract class NodoOperando extends NodoExpresion {
    protected Token token;
    public NodoOperando(Token token){
        this.token = token;
    }

    public abstract AbstractType check() throws SemanticException;
}
