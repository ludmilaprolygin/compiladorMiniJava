package model.AST.Encadenados;

import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public class EncadenadoVacio extends Encadenado{
    @Override
    public void setEncadenado(Encadenado encadenado) {

    }

    @Override
    public AbstractType check(AbstractType t) throws SemanticException {
        return t;
    }
    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + ". {encadenado vacio}\n";
    }
}
