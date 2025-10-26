package model.AST.Expresiones;

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
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += operador.getLexeme() + "\n";
        toReturn += condicion.toString(depth + 1);
        toReturn += sTrue.toString(depth + 1);
        toReturn += sFalse.toString(depth + 1);
        return toReturn;
    }

    @Override
    public Token getToken() {
        return operador;
    }
}
