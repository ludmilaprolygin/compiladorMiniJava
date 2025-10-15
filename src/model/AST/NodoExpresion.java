package model.AST;

import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public abstract class NodoExpresion {
    public abstract AbstractType check() throws SemanticException;
}
