package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public abstract class Encadenado {
    protected Token nombre;
    protected Encadenado encadenado;
    public abstract void setEncadenado(Encadenado encadenado);
    public Token getNombre() { return nombre; }
    public abstract void check(AbstractType t) throws SemanticException;
}
