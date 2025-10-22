package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoVar extends NodoOperando {
    protected AbstractType tipo;
    protected Encadenado encadenado;
    protected boolean isDeclared;
    public NodoVar(Token token) {
        super(token);
        tipo = new UniversalType();
    }
    public NodoVar(Token token, AbstractType tipo) {
        super(token);
        this.tipo = tipo;
    }

    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    public void setTipo (AbstractType a){
        tipo = a;
    }
    public void declare() { isDeclared = true;}

    @Override
    public AbstractType check() throws SemanticException {
        isDeclared();
        if(encadenado != null)
            encadenado.check(tipo);
        return tipo;
    }

    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + token.getLexeme() + " (" + tipo.getClass() + ")\n";
    }

    public boolean isDeclared() throws SemanticException {
        return isDeclared || super.isDeclared();
    }
}
