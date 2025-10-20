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
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + "NULL \n";
    }
}
