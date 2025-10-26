package model.AST.Sentencias;

import utils.exceptions.SemanticException;

public abstract class NodoSentencia {
    public abstract void check() throws SemanticException;
    public abstract String toString(int depth);
}
