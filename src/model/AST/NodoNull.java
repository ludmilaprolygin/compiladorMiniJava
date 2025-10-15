package model.AST;

import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoNull extends NodoOperando{
    public NodoNull(){
        super(null);
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }
}
