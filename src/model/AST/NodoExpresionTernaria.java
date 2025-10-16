package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoExpresionTernaria extends NodoExpresion{

    protected Token operador;
    protected NodoExpresion condicion;
    protected NodoExpresion sTrue;
    protected NodoExpresion sFalse;

    public NodoExpresionTernaria(Token t, NodoExpresion j, NodoExpresion i, NodoExpresion d){
        operador = t;
        condicion = j;
        sTrue = i;
        sFalse = d;
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += operador.getLexeme() + "\n";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += condicion.toString(depth + 1);
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += sTrue.toString(depth + 1);
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += sFalse.toString(depth + 1);
        return toReturn;
    }
}
