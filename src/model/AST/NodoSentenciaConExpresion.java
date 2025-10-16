package model.AST;

import utils.exceptions.SemanticException;

public class NodoSentenciaConExpresion extends NodoSentencia {
    protected NodoExpresion expresion;
    public NodoSentenciaConExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public void check() throws SemanticException {
        expresion.check();
    }

    @Override
    public String toString(int depth) {
        return expresion.toString(depth);
    }
}
