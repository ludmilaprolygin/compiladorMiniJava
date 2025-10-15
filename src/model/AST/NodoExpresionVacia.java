package model.AST;

import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoExpresionVacia extends NodoExpresion {
    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }
}
