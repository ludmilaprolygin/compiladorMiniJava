package model.AST;

import model.symbolTable.AbstractType;
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
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += "=\n";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += ladoIzquierdo.toString(depth + 1) + "\n";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += ladoDerecho.toString(depth + 1) + "\n";
        return toReturn;
    }
}
