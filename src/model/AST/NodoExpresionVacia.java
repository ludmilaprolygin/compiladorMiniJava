package model.AST;

import utils.exceptions.SemanticException;

public class NodoExpresionVacia extends NodoExpresion {
    @Override
    public void check() throws SemanticException { }
}
