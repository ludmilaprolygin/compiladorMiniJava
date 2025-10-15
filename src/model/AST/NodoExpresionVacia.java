package model.AST;

import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoExpresionVacia extends NodoExpresion {
    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        return toReturn + "{ nodo expresion vacia }\n";
    }
}
