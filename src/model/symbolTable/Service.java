package model.symbolTable;

import model.AST.Sentencias.NodoBloque;
import model.AST.Sentencias.NodoBloqueVacio;
import model.Token;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIMessage;

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

    public void gen(OutputManager o, String className) throws GenerationException {
        String myName;
        if (this instanceof Builder b){
            myName = "builder@" + className;
        } else {
            myName = name.getLexeme() + "@" + className;
        }

        o.gen("lbl_" + myName + ": " + Instructions.NOP);

        o.gen("");
    }
}
