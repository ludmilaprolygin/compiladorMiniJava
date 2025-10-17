package model.AST;

import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoLLamadaConstructor extends NodoExpresion{
    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }

    @Override
    public String toString(int depth) {
        return "";
    }
}
