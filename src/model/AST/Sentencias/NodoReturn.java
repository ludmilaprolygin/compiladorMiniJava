package model.AST.Sentencias;

import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoExpresionVacia;
import model.AST.Expresiones.NodoThis;
import model.Token;
import model.symbolTable.*;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import static model.TokenType.reservedVoid;

public class NodoReturn extends NodoSentencia {
    protected NodoExpresion expresion;
    protected Token t;

    public NodoReturn(NodoExpresion e, Token token){
        expresion = e;
        t = token;
    }

    @Override
    public void check() throws SemanticException {
        AbstractType aType = expresion.check();

        if(!(expresion instanceof NodoExpresionVacia)) {
            Service s = SymbolTable.symbolTable().getCurrentService();
            if (s instanceof Method m)
                try {
                    m.getReturnType().compatible(aType);
                } catch (Exception e) {
                    throw new SemanticException(SemanticErrorIIMessage.incorrectReturnType(t, "return"));
                }
        }
    }

    public Token getToken() { return t; }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "return\n";
        toReturn += expresion.toString(depth + 1);
        return toReturn;
    }

    @Override
    public void checkThisOnStaticContext() throws SemanticException {
        if(expresion instanceof NodoThis){
            throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(expresion.getToken()));
        }
    }

    public boolean compatibleWithVoid() {
        return expresion instanceof NodoExpresionVacia;
    }

    public boolean compatibleWithType(AbstractType type) throws SemanticException {
        AbstractType returnType = expresion.check();
        if(type.getName().getLexeme().equals(reservedVoid.getTypeExplanation())){
            return compatibleWithVoid();
        }
        else
            return returnType.compatible(type);
    }

    public AbstractType getType() throws SemanticException {
        return expresion.check();
    }

    public NodoExpresion getExpresion() { return expresion; }

    public NodoReturn hasReturnStatementSomewhere() {
        return this;
    }

    @Override
    public void gen(OutputManager o) {

    }
}
