package model.AST;

import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public abstract class Encadenado {
    public abstract void check(AbstractType t) throws SemanticException;
}
