package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class Constructor extends Element {
    public Constructor(Token n) {
        super(n);
    }

    @Override
    public void correctDeclaration() throws SemanticException {

    }
}
