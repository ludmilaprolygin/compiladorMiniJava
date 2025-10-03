package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public abstract class Service extends Element {
    protected Token visibility;
    protected List parameters;
    public Service(Token n, Token v) {
        super(n);
        visibility = v;
        parameters = new List();
    }

    public void correctDeclaration() throws SemanticException {
        for(Element p : parameters)
            p.correctDeclaration();
    }

    public void addParameter(Parameter p) throws SemanticException{
        if(parameters.contains(p.getName().getLexeme()))
            throw new SemanticException(SemanticErrorMessage.parameterAlreadyExists(p.getName()));
        parameters.addLast(p);
    }

    public List getParameters() { return parameters; }

    public Token getVisibility() { return visibility; }

    public boolean equalSignature(Service service){
        if(!this.name.getLexeme().equals(service.name.getLexeme()))
            return false;
        else {
            return parameters.equals(service.parameters);
        }
    }
}
