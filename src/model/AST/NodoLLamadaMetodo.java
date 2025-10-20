package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.Method;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

import static model.symbolTable.SymbolTable.symbolTable;

public class NodoLLamadaMetodo extends NodoExpresion {
    protected Token metodo;
    protected java.util.List<NodoExpresion> argumentos;

    public NodoLLamadaMetodo(Token m, java.util.List<NodoExpresion> a){
        metodo = m;
        argumentos = a;
    }

    @Override
    public AbstractType check() throws SemanticException {
        return ((Method) symbolTable().getCurrentService()).getReturnType();
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        if (argumentos.isEmpty())
            return toReturn + metodo.getLexeme() + "() \n";
        else
            return toReturn + metodo.getLexeme() + "(" + argumentos.toString()+ ")\n";
    }
}
