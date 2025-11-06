package model.AST.Sentencias;

import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoThis;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoSentenciaConExpresion extends NodoSentencia {
    protected NodoExpresion expresion;
    public NodoSentenciaConExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public void check() throws SemanticException {
        expresion.check();
    }

    @Override
    public String toString(int depth) {
        return expresion.toString(depth);
    }

    @Override
    protected void checkThisOnStaticContext() throws SemanticException {
        if(expresion instanceof NodoThis){
            throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(expresion.getToken()));
        }
    }

    @Override
    public void gen(OutputManager o) {
        expresion.gen(o);
    }
}
