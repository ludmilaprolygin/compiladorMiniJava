package model.AST.Operandos;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Expresiones.NodoExpresion;
import model.AST.Sentencias.Bloques.NodoBloque;
import model.Token;
import model.symbolTable.*;
import outputManager.OutputManager;
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
        SymbolTable st = SymbolTable.symbolTable();
        String varName = this.getToken().getLexeme();
        AbstractType aType = null;
        boolean toReturn = false;

        NodoBloque bloque = st.getBloque();
        while(bloque != null && !toReturn){
            for(NodoOperando n : bloque.getVariables()){
                if(n.getToken().getLexeme().equals(varName)){

                    if (n instanceof NodoVar v) {
                        aType = v.getType();
                        v.setVar(v);
                    }
                    else {
                        aType = new UniversalType();
                    }

                    toReturn = true;
                    break;
                }
            }
            if (!toReturn)
                bloque = bloque.getBloqueContenedor();
        }

        if(!toReturn) {
            for(Element n : st.getCurrentService().getParameters()){
                if(n.getName().getLexeme().equals(varName)){
                    aType = ((model.symbolTable.Parameter) n).getType();
                    toReturn = true;
                    if(this instanceof NodoVar v){
                        v.setVar(((model.symbolTable.Parameter) n));
                    }
                    break;
                }
            }
        }

        if(!toReturn) {
            for(Element e : st.getCurrentClass().getAttributes().values()){
                Attribute n = (Attribute) e;
                if(n.getName().getLexeme().equals(varName)){
                    aType = n.getType();
                    toReturn = true;
                    if(this instanceof NodoVar v){
                        v.setVar(n);
                    }
                    break;
                }
            }
        }

        if (!toReturn){
            throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(getToken()));
        }

        return aType;
    }

    @Override
    public Encadenado getLastEncadenado() {
        return new EncadenadoVacio();
    }

    public void gen(OutputManager o) { }
}
