package model.AST.Sentencias;

import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoExpresionVacia;
import model.Token;
import utils.exceptions.SemanticException;

public class NodoReturn extends NodoSentencia {
    protected NodoExpresion expresion;
    protected Token t;

    public NodoReturn(NodoExpresion e, Token token){
        expresion = e;
        t = token;
    }

    @Override
    public void check() throws SemanticException {
        expresion.check();
    }

    public Token getToken() { return t; }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "return\n";
        toReturn += expresion.toString(depth + 1);
            return toReturn;
    }

    public boolean compatibleWithVoid() {
        return expresion instanceof NodoExpresionVacia;
    }
}
