package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Method extends Element {
    private Token visibility;
    private MethodType returnType;
    private Token modifier;
    private Table<Parameter> parameters;

    public Method (Token n, Token v, Token m, MethodType t) {
        super(n);
        visibility = v;
        returnType = t;
        modifier = m;
        parameters = new Table<>();
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
