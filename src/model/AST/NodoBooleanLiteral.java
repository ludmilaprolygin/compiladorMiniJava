package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import utils.exceptions.SemanticException;

public class NodoBooleanLiteral extends NodoOperando {
    public NodoBooleanLiteral(Token token){
        super(token);
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new BooleanType(token);
    }

}
