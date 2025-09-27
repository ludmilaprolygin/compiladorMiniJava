package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class Parameter extends Element{

    public Parameter(Token n) {
        super(n);
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        ;
    }
}
