package model.AST.Expresiones;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public abstract class NodoExpresion {
    public abstract AbstractType check() throws SemanticException;
    public abstract String toString(int depth);

    public abstract Token getToken();
}
