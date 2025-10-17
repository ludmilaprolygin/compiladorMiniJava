package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.Method;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoLLamadaMetodo extends NodoExpresion {
    protected Token metodo;
    protected java.util.List<NodoExpresion> argumentos;

    public NodoLLamadaMetodo(Token m, java.util.List<NodoExpresion> a){
        metodo = m;
        argumentos = a;
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }

    @Override
    public String toString(int depth) {
        return "";
    }
}
