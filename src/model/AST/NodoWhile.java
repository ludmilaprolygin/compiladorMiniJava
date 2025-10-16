package model.AST;

import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import utils.exceptions.SemanticException;

public class NodoWhile extends NodoSentencia {
    protected NodoExpresion condicion;
    protected NodoSentencia sentencia;

    public NodoWhile(NodoExpresion c, NodoSentencia s){
        condicion = c;
        sentencia = s;
    }

    @Override
    public void check() throws SemanticException {
        AbstractType expType = condicion.check();
        expType.compatible(new BooleanType(null));

        sentencia.check();
    }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += "while\n";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += condicion.toString(depth + 1) + "\n";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += sentencia.toString(depth + 1) + "\n";
        return toReturn;
    }
}
