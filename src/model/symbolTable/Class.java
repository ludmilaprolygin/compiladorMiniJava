package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public class Class extends MainElement {
    private Table<Constructor> constructors;
    public Class(Token m, Token n, Token i) {
        super(m, n, i);
        constructors = new Table<>();
    }

    public void addConstructor(Token t, Constructor c) throws SemanticException {
        if(!constructors.contains(t.getLexeme()))
            constructors.put(t, c);
        else {
            throw new SemanticException(SemanticErrorMessage.constructorAlreadyExists(t));
        }
    }
}
