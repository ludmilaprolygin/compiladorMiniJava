package model.AST;

import utils.exceptions.SemanticException;

public abstract class NodoExpresion {
    public abstract void check() throws SemanticException;
}
