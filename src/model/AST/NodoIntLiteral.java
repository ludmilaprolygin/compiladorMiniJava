package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.IntType;
import utils.exceptions.SemanticException;

public class NodoIntLiteral extends NodoOperando{
    public NodoIntLiteral(Token token){
        super(token);
    }

    public AbstractType check() throws SemanticException {
        return new IntType(token);
    }
}
