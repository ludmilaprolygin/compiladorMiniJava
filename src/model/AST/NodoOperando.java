package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.SymbolTable;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public abstract class NodoOperando extends NodoExpresion {
    protected Token token;
    public NodoOperando(Token token){
        this.token = token;
    }
    public Token getToken() { return token; }
    public abstract AbstractType check() throws SemanticException;
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + token.getLexeme() + "\n";
    }

    protected boolean isDeclared() throws SemanticException {
        boolean toReturn = false;
        SymbolTable st = SymbolTable.symbolTable();
        toReturn = st.getBloque().getVariables().contains(this);
        toReturn = toReturn || st.getCurrentService().getParameters().contains(this.getToken().getLexeme());
        toReturn = toReturn || st.getCurrentClass().getAttributes().contains(this.getToken().getLexeme());
        if (!toReturn){
            throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(getToken()));
        }
        return toReturn;
    }
}
