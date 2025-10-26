package model.AST;

import model.AST.Expresiones.NodoExpresion;
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
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "while\n";
        toReturn += condicion.toString(depth + 1);
        toReturn += sentencia.toString(depth + 1);
        return toReturn;
    }
}
