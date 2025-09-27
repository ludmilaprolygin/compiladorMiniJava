package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

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

    @Override
    public void correctDeclaration() throws SemanticException {
        if(inheritance != null && !symbolTable().getClasses().contains(inheritance.getLexeme()))
            throw new SemanticException(SemanticErrorMessage.parentDoesNotExist(inheritance));
        for (Attribute a : attributes.values())
            a.correctDeclaration();
        for (Method m : methods.values())
            m.correctDeclaration();
        for (Constructor c : constructors.values())
            c.correctDeclaration();
    }
}
