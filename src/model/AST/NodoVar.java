package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

public class NodoVar extends NodoOperando {
    protected AbstractType tipo;
    public NodoVar(Token token) {
        super(token);
        tipo = new UniversalType();
    }
    public NodoVar(Token token, AbstractType tipo) {
        super(token);
        this.tipo = tipo;
    }
    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }
}
