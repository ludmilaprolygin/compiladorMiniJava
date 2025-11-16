package model.AST.Encadenados;

import model.Token;
import model.symbolTable.AbstractType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public abstract class Encadenado {
    protected Token nombre;
    protected Encadenado encadenado;
    public Encadenado(Token t){
        nombre = t;
        encadenado = new EncadenadoVacio();
    }
    public Encadenado(Token t, Encadenado e){
        nombre = t;
        encadenado = e;
    }
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
        toReturn += "." + nombre.getLexeme() + " " + getClass().getSimpleName() + "\n" + encadenado.toString(depth + 1);
        return toReturn;
    }

    public Encadenado getLastEncadenado() {
        Encadenado current = this;
        Encadenado toReturn = this;
        while (current != null && !(current.getEncadenado() instanceof EncadenadoVacio)) {
            current = current.getEncadenado();
            if(current != null)
                toReturn = current;
        }
        return toReturn;
    }

    public abstract void gen(OutputManager o, AbstractType tipo);
}
