package model.AST.Sentencias;

import model.AST.Expresiones.*;
import model.codeGeneration.Comments;
import model.codeGeneration.Instructions;
import model.symbolTable.AbstractType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import static model.TokenType.reservedVoid;

public class NodoSentenciaConExpresion extends NodoSentencia {
    public NodoExpresion expresion;
    private AbstractType tipoExpresion;
    public NodoSentenciaConExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public void check() throws SemanticException {
        tipoExpresion = expresion.check();
    }

    @Override
    public String toString(int depth) {
        return expresion.toString(depth);
    }

    @Override
    public void checkThisOnStaticContext() throws SemanticException {
        if(expresion instanceof NodoThis){
            throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(expresion.getToken()));
        }
    }

    @Override
    public void gen(OutputManager o) {
        expresion.gen(o);

        if(!(tipoExpresion.getName().getLexeme().equals(reservedVoid.getTypeExplanation()))){
            if(!(expresion instanceof NodoExpresionAsignacion) && !(expresion instanceof NodoExpresionUnaria) && !(expresion instanceof NodoExpresionBinaria)){
                o.gen(Instructions.POP.toString() + Comments.FREE_RETURN_VALUE.getComment());
            }
        }
    }
}
