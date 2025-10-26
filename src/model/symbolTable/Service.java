package model.symbolTable;

import model.AST.NodoBloque;
import model.AST.NodoBloqueVacio;
import model.AST.NodoOperando;
import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;
import utils.messages.SemanticErrorIMessage;

import java.util.LinkedList;

import static model.symbolTable.SymbolTable.symbolTable;

public abstract class Service extends Element {
    protected Token visibility;
    protected List parameters;
    protected NodoBloque bloque;
    public Service(Token n, Token v) {
        super(n);
        visibility = v;
        parameters = new List();
        bloque = new NodoBloqueVacio(null);
    }

    public void correctDeclaration() throws SemanticException {
        for(Element p : parameters)
            p.correctDeclaration();
    }

    public void addParameter(Parameter p) throws SemanticException{
        if(parameters.contains(p.getName().getLexeme()))
            throw new SemanticException(SemanticErrorIMessage.parameterAlreadyExists(p.getName()));
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

    public void setBloque(NodoBloque b) { bloque = b; }
    public NodoBloque getBloque() { return bloque; }

    public void check() throws SemanticException {
        symbolTable().setBloque(bloque);
        bloque.check();
    }
}
