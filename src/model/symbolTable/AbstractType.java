package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public abstract class AbstractType extends Element {
    public AbstractType(Token n) {
        super(n);
    }

    public void correctDeclaration() throws SemanticException {
        if(name.getTokenType().equals(TokenType.idClase) && !SymbolTable.symbolTable().getClasses().contains(name.getLexeme()))
            throw new SemanticException(SemanticErrorMessage.undeclaredType(name));
    }

    public boolean equals(AbstractType t) {
        return name.getLexeme().equals(t.getName().getLexeme());
    }
}
