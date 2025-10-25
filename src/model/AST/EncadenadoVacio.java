package model.AST;

import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class EncadenadoVacio extends Encadenado{
    @Override
    public void setEncadenado(Encadenado encadenado) {

    }

    @Override
    public AbstractType check(AbstractType t) throws SemanticException {
        return t;
    }
}
