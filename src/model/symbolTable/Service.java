package model.symbolTable;

import model.AST.Operandos.NodoOperando;
import model.AST.Operandos.NodoVar;
import model.AST.Sentencias.Bloques.NodoBloque;
import model.AST.Sentencias.Bloques.NodoBloqueVacio;
import model.Token;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public abstract class Service extends OffsetElement {
    protected Token visibility;
    protected List parameters;
    protected NodoBloque bloque;
    protected int localVarCount;
    private int firstFreeMemoryAddress = 0;


    public Service(Token n, Token v) {
        super(n);
        visibility = v;
        parameters = new List();
        bloque = new NodoBloqueVacio(null);
        localVarCount = 0;
    }

    public void correctDeclaration() throws SemanticException {
        for(Element p : parameters)
            p.correctDeclaration();
    }

    public void incLocalVarCount() { localVarCount++; }

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

    public void setFirstFreeMemoryAddress(int address) { firstFreeMemoryAddress = address; }
    public int getFirstFreeMemoryAddress() { return firstFreeMemoryAddress; }

    public void setBloque(NodoBloque b) { bloque = b; }
    public NodoBloque getBloque() { return bloque; }

    public void check() throws SemanticException {
        symbolTable().setBloque(bloque);
        bloque.check();
    }

    public void gen(OutputManager o, String className) throws GenerationException {
        symbolTable().setCurrentService(this);

        String myName;
        if (this instanceof Builder b){
            myName = "builder@" + className;
        } else {
            myName = name.getLexeme() + "@" + className;
        }

        o.gen("lbl_" + myName + ": ");

//        System.out.println("Generating code for service: lbl_" + myName);
//        o.printStackTop();
//        System.out.println("-------------------------");

        firstFreeMemoryAddress = 0;
        gen(o);

        o.gen("");
    }

    public AbstractType searchType(Var v) {
        for(NodoOperando o : bloque.getVariables()){
            if(o.getToken().getLexeme().equals(v.getTokenName().getLexeme())){
                if(o instanceof NodoVar var){
                    return var.getType();
                }
            }
        }
        for(OffsetElement p : parameters){
            if(p.getName().getLexeme().equals(v.getTokenName().getLexeme())) {
                return p.getType();
            }
        }
        return null;
    }

    public abstract void gen(OutputManager o) throws GenerationException;
}
