package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Interface extends MainElement {
    public Interface(Token m, Token n, Type t, Token i) {
        super(m, n, t, i);
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        if(inheritance != null && !symbolTable().getInterfaces().contains(inheritance.getLexeme()))
            if(inheritance != null && symbolTable().getClasses().contains(inheritance.getLexeme()))
                throw new SemanticException(SemanticErrorMessage.interfaceExtendingAClass(inheritance));
            else
                throw new SemanticException(SemanticErrorMessage.parentDoesNotExist(inheritance));
        for (Attribute a : attributes.values())
            a.correctDeclaration();
        for (Method m : methods.values())
            m.correctDeclaration();
    }
}
