package model.AST.Operandos;

import model.symbolTable.AbstractType;
import model.symbolTable.Parameter;
import utils.exceptions.SemanticException;

public class NodoParametro extends NodoOperando{
    private Parameter a;

    public NodoParametro(Parameter a) {
        super(a.getName());
        this.a = a;
    }

    @Override
    public AbstractType check() throws SemanticException {
        return a.getType();
    }
}
