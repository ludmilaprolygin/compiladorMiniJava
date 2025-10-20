package model.AST;

import utils.exceptions.SemanticException;

public class NodoReturn extends NodoSentencia {
    protected NodoExpresion expresion;

    public NodoReturn(NodoExpresion e){
        expresion = e;
    }

    @Override
    public void check() throws SemanticException {

    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "return\n";
        toReturn += expresion.toString(depth + 1);
            return toReturn;
    }
}
