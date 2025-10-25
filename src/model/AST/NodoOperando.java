package model.AST;

import model.Token;
import model.symbolTable.*;
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

    protected AbstractType isDeclared() throws SemanticException {
        boolean toReturn = false;
        AbstractType aType = new UniversalType();
        SymbolTable st = SymbolTable.symbolTable();
        toReturn = st.getBloque().getVariables().contains(this);
        for(NodoOperando n : st.getBloque().getVariables()){
            if(n.getToken() != null && this.getToken() != null && n.getToken().getLexeme().equals(this.getToken().getLexeme())){
                toReturn = true;
                aType = n.check();
            }
        }
        NodoBloque bloque = st.getBloque();
        while(!toReturn && bloque != null && bloque != st.getCurrentService().getBloque()){
            toReturn = toReturn || bloque.getVariables().contains(this);
            for(NodoOperando n : st.getBloque().getVariables()){
                if(n.getToken() != null && this.getToken() != null && n.getToken().getLexeme().equals(this.getToken().getLexeme())){
                    aType = n.check();
                    toReturn = true;
                }
            }
            bloque = bloque.getBloqueContenedor();
        }
        toReturn = toReturn || st.getCurrentService().getParameters().contains(this.getToken().getLexeme());
        for(Element n : st.getCurrentService().getParameters()){
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme())){
                aType = ((model.symbolTable.Parameter) n).getType();
                toReturn = true;
            }
        }
        toReturn = toReturn || st.getCurrentClass().getAttributes().contains(this.getToken().getLexeme());
        for(Attribute n : st.getCurrentClass().getAttributes().values()){
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme())){
                aType = n.getType();
                toReturn = true;
            }
        }
        if (!toReturn){
            throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(getToken()));
        }
        return aType;
    }
}
