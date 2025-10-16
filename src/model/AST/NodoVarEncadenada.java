package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public class NodoVarEncadenada extends Encadenado{
    protected Token nombre;
    protected Encadenado encadenado;
    public NodoVarEncadenada(Token t){
        nombre = t;
    }
    public NodoVarEncadenada(Token t, Encadenado e){
        nombre = t;
        encadenado = e;
    }
    public void setEncadenado(Encadenado encadenado){
        this.encadenado = encadenado;
    }

    @Override
    public void check(AbstractType t) throws SemanticException {

    }
}
