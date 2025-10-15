package model.AST;

import utils.exceptions.SemanticException;

public abstract class NodoSentencia {
    public abstract void check() throws SemanticException;
}
