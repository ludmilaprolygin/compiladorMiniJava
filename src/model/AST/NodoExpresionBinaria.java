package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public class NodoExpresionBinaria extends NodoExpresion {
    protected Token operador;
    protected NodoExpresion ladoIzquierdo;
    protected NodoExpresion ladoDerecho;

    public NodoExpresionBinaria(Token t, NodoExpresion i, NodoExpresion d){
        operador = t;
        ladoIzquierdo = i;
        ladoDerecho = d;
    }

    @Override
    public AbstractType check() throws SemanticException {
        return null;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += operador.getLexeme() + "\n";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += ladoIzquierdo.toString(depth + 1);
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += ladoDerecho.toString(depth + 1);
        return toReturn;
    }
}
