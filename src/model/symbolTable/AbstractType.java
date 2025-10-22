package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;
import utils.messages.SemanticErrorIMessage;

public abstract class AbstractType extends Element {
    public AbstractType(Token n) {
        super(n);
    }

    public void correctDeclaration() throws SemanticException {
        if(SymbolTable.symbolTable().getCurrentClass().getParametricType() != null && SymbolTable.symbolTable().getCurrentClass().getParametricType().getName().getLexeme().equals(name.getLexeme()));
        else if(name.getTokenType().equals(TokenType.idClase) && (!SymbolTable.symbolTable().getClasses().contains(name.getLexeme()) && !SymbolTable.symbolTable().getInterfaces().contains(name.getLexeme())))
            throw new SemanticException(SemanticErrorIMessage.undeclaredType(name));
    }

    public boolean equals(AbstractType t) {
        return name.getLexeme().equals(t.getName().getLexeme());
    }

    public boolean compatible(AbstractType t) throws SemanticException {
        System.out.println("Comparing " + this.getClass() + " with " + t.getClass());
        if (t.getClass() != getClass())
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(this.getName()));
        return true;
    }
}
