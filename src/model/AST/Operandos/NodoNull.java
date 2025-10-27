package model.AST.Operandos;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.Class;
import model.symbolTable.ClassType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoNull extends NodoOperando{
    public NodoNull(){
        super(null);
    }

    @Override
    public AbstractType check() throws SemanticException {
        Token tk = new Token(null, "Object", -1);
        return new ClassType(tk);
    }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + "NULL \n";
    }
}
