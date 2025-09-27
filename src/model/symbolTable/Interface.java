package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class Interface extends MainElement {
    public Interface(Token m, Token n, Token i) {
        super(m, n, i);
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        ;
    }
}
