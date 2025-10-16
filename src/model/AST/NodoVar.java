package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoVar extends NodoOperando {
    protected AbstractType tipo;
    protected Encadenado encadenado;
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

    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }
}
