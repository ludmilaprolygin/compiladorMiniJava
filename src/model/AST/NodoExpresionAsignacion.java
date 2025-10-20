package model.AST;

import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoExpresionAsignacion extends NodoExpresion{
    protected NodoExpresion ladoIzquierdo;
    protected NodoExpresion ladoDerecho;

    public NodoExpresionAsignacion(NodoExpresion l, NodoExpresion r){
        ladoIzquierdo = l;
        ladoDerecho = r;
    }
    @Override
    public AbstractType check() throws SemanticException {
        AbstractType left = ladoIzquierdo.check();
        AbstractType right = ladoDerecho.check();
        if(!left.compatible(right))
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(left.getName()));

        return right;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "=\n";
        toReturn += ladoIzquierdo.toString(depth + 1);
        toReturn += ladoDerecho.toString(depth + 1);
        return toReturn;
    }
}
