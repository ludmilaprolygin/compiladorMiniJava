package model.AST.Operandos;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.ClassType;
import utils.exceptions.SemanticException;

public class NodoStringLiteral extends NodoOperando{

    public NodoStringLiteral(Token token) {
        super(token);
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new ClassType(token);
    }
}
