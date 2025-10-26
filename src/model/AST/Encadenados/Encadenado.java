package model.AST.Encadenados;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

public abstract class Encadenado {
    protected Token nombre;
    protected Encadenado encadenado;
    public abstract void setEncadenado(Encadenado encadenado);
    public Token getNombre() { return nombre; }
    public Encadenado getEncadenado() { return encadenado; }
    public abstract AbstractType check(AbstractType t) throws SemanticException;
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        if(encadenado == null)
            encadenado = new EncadenadoVacio();
        toReturn += "." + nombre.getLexeme() + "\n" + encadenado.toString(depth + 1);
        return toReturn;
    }
}
