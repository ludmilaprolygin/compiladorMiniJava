package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import java.util.LinkedList;
import java.util.List;

public abstract class Service extends Element {
    protected Token visibility;
    protected Table<Parameter> parameters; //TODO - cambiar a lista
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
        if(parameters.contains(p.getName().getLexeme()))
            throw new SemanticException(SemanticErrorMessage.parameterAlreadyExists(p.getName()));
        parameters.put(p.getName(), p);
    }

    public Table<Parameter> getParameters() { return parameters; }

    public Token getVisibility() { return visibility; }

    public boolean equalSignature(Service service){
        if(!this.name.getLexeme().equals(service.name.getLexeme()))
            return false;
        else {
            return parameters.equals(service.parameters);
        }
    }
}
