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
    protected model.symbolTable.MainElement belongingClass;

    public NodoLLamadaMetodo(Token m, java.util.List<NodoExpresion> a, model.symbolTable.MainElement c){
        metodo = m;
        argumentos = a;
        belongingClass = c;
    }

    public Token getMetodo() { return metodo; }

    @Override
    public AbstractType check() throws SemanticException {
        Token tokenM = belongingClass.getMethods().getTokenByName(metodo.getLexeme());
        Method m = belongingClass.getMethods().get(tokenM);
        return m.getReturnType();
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

    @Override
    public Token getToken() {
        return metodo;
    }
}
