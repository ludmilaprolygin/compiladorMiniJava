package model.AST;

import utils.exceptions.SemanticException;

public class NodoIf extends NodoSentencia {
    protected NodoExpresion expresion;
    protected NodoSentencia sentenciaIf;
    protected NodoSentencia sentenciaElse;
    public NodoIf(NodoExpresion e, NodoSentencia sIf, NodoSentencia sElse) {
        expresion = e;
        sentenciaIf = sIf;
        sentenciaElse = sElse;
    }
    @Override
    public void check() throws SemanticException {
        //TODO - completar
    }
}
