package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoThis extends NodoExpresion {
    protected Encadenado encadenado;
    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }

    @Override
    public String toString(int depth) {
        return "";
    }

    @Override
    public Token getToken() {
        return null;
    }

    public void setEncadenado(Encadenado e){
        encadenado = e;
    }
}
