package model.AST;

import model.Token;
import model.symbolTable.Attribute;
import model.symbolTable.Element;
import model.symbolTable.Parameter;
import model.symbolTable.Service;
import utils.exceptions.SemanticException;
import utils.exceptions.SyntacticException;
import utils.messages.SemanticErrorIIMessage;

import java.util.LinkedList;
import java.util.List;

import static model.symbolTable.SymbolTable.symbolTable;

public class NodoBloque extends NodoSentencia {

    protected final List<NodoSentencia> statements;
    protected List<NodoOperando> variables;
    protected NodoBloque bloqueContenedor;
    protected List<NodoLLamadaMetodo> llamadas;

    public NodoBloque() {
        statements = new LinkedList<>();
        variables = new LinkedList<>();
        llamadas = new LinkedList<>();
    }

    public List<NodoSentencia> getStatements() { return statements; }
    public List<NodoOperando> getVariables() { return variables; }
    public void addVariable(NodoOperando variable) throws SemanticException {
        for(NodoOperando o : variables){
            if(o.getToken().getLexeme().equals(variable.getToken().getLexeme()))
                throw new SemanticException(SemanticErrorIIMessage.variableAlreadyExists(variable.getToken()));
        }
        variables.addLast(variable);
    }

    public void setBloqueContenedor(NodoBloque bloque) {
        this.bloqueContenedor = bloque;
    }

    public NodoBloque getBloqueContenedor() {
        return bloqueContenedor;
    }

    public void addStatement (NodoSentencia statement) {
        statements.add(statement);
    }
    public void check() throws SemanticException {
        NodoBloque bloqueAnterior = symbolTable().getBloque();
        symbolTable().setBloque(this);
        for(NodoSentencia s : statements)
            s.check();
        symbolTable().setBloque(bloqueAnterior);
    }

    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += " ";
        toReturn = "{\n";
        for(NodoSentencia s : statements){
            toReturn += s.toString(depth + 1) + "\n";
        }
        for (int i = 0; i < depth; i++)
            toReturn += " ";
        toReturn += "}";
        return toReturn;
    }

    public NodoExpresion variableExists(Token currentToken) throws SemanticException {
        for (NodoOperando var : variables) {
            if (var.getToken().getLexeme().equals(currentToken.getLexeme())) {
                return var;
            }
        }
        if (bloqueContenedor != null && bloqueContenedor != this) {
            return bloqueContenedor.variableExists(currentToken);
        }
        else {
            Service s = symbolTable().getCurrentService();
            for(Element param : s.getParameters()) {
                if (((Parameter) param).getName().getLexeme().equals(currentToken.getLexeme())) {
                    for(NodoLLamadaMetodo llamada : llamadas) {
                        if(llamada.getMetodo().getLexeme().equals(param.getName().getLexeme())) {
                            return llamada;
                        }
                    }
                }
            }
            System.out.println(symbolTable().getCurrentClass().getAttributes());
            Token t = symbolTable().getCurrentClass().getAttributes().getTokenByName(currentToken.getLexeme());
            if (t != null) {
                return new NodoVar(currentToken);
            }

        }
        throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(currentToken));
    }

    public void addLlamada(NodoExpresion toReturn) {
        llamadas.add((NodoLLamadaMetodo) toReturn);
    }
}
