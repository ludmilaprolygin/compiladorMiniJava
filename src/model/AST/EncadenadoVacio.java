package model.AST;

import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public class EncadenadoVacio extends Encadenado{
    @Override
    public void setEncadenado(Encadenado encadenado) {

    }

    @Override
    public void check(AbstractType t) throws SemanticException {

    }
}
