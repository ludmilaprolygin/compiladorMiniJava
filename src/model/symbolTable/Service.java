package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public abstract class Service extends Element {
    protected Token visibility;
    protected Table<Parameter> parameters;
    public Service(Token n, Token v) {
        super(n);
        visibility = v;
        parameters = new Table<>();
    }

    public void correctDeclaration() throws SemanticException {
        for(Parameter p : parameters.values())
            p.correctDeclaration();
    }

    public void addParameter(Parameter p) throws SemanticException{
        if(parameters.containsKey(p.getName()))
            throw new SemanticException(SemanticErrorMessage.parameterAlreadyExists(p.getName()));
        parameters.put(p.getName(), p);
    }
}
