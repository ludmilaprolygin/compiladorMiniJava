package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Method extends Service {
    private AbstractType returnType;
    private Token modifier;

    public Method (Token n, Token v, Token m, AbstractType t) {
        super(n, v);
        returnType = t;
        modifier = m;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        if(symbolTable().getCurrentClass().getMethods().contains(name.getLexeme()))
            throw new SemanticException(SemanticErrorMessage.methodAlreadyExists(name));
        returnType.correctDeclaration();
        for(Parameter p : parameters.values())
            p.correctDeclaration();
    }
}
