package model.AST;

import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import java.util.LinkedList;
import java.util.List;

public class NodoBloque extends NodoSentencia {

    protected final List<NodoSentencia> statements;
    protected List<NodoOperando> variables;

    public NodoBloque() {
        statements = new LinkedList<>();
        variables = new LinkedList<>();
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

    public void addStatement (NodoSentencia statement) {
        statements.add(statement);
    }
    public void check() throws SemanticException {
        for(NodoSentencia s : statements)
            s.check();
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
}
