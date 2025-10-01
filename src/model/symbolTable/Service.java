package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Service extends Element {
    protected Token visibility;
    protected List<Parameter> parameters; //TODO - cambiar a lista
    public Service(Token n, Token v) {
        super(n);
        visibility = v;
        parameters = new LinkedList<>();
    }

    public void correctDeclaration() throws SemanticException {
        for(Parameter p : parameters)
            p.correctDeclaration();
    }

    public void addParameter(Parameter p) throws SemanticException{
        if(parameters.contains(p))
            throw new SemanticException(SemanticErrorMessage.parameterAlreadyExists(p.getName()));
        parameters.addLast(p);
    }

    public List<Parameter> getParameters() { return parameters; }

    public boolean equalSignature(Service service){
        if(!this.name.getLexeme().equals(service.name.getLexeme()))
            return false;
        return this.parameters.size() == service.getParameters().size();
    }
}
