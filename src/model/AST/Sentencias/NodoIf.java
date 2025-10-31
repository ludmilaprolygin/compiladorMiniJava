package model.AST.Sentencias;

import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoThis;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoIf extends NodoSentencia {
    protected NodoExpresion condicion;
    protected NodoSentencia sentenciaIf;
    protected NodoSentencia sentenciaElse;
    public NodoIf(NodoExpresion c, NodoSentencia sIf, NodoSentencia sElse) {
        condicion = c;
        sentenciaIf = sIf;
        sentenciaElse = sElse;
    }
    @Override
    public void check() throws SemanticException {
        AbstractType expType = condicion.check();
        expType.compatible(new BooleanType(null));

        sentenciaIf.check();
        sentenciaElse.check();
    }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "if\n";
        toReturn += condicion.toString(depth + 1);
        toReturn += sentenciaIf.toString(depth + 1);
        toReturn += sentenciaElse.toString(depth + 1);
        return toReturn;
    }

    @Override
    protected void checkThisOnStaticContext() throws SemanticException {
        if(condicion instanceof NodoThis){
            throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(condicion.getToken()));
        }
        sentenciaIf.checkThisOnStaticContext();
        sentenciaElse.checkThisOnStaticContext();
    }

    public NodoReturn hasReturnStatementSomewhere() {
        NodoReturn rIf = sentenciaIf.hasReturnStatementSomewhere();
        NodoReturn rElse = sentenciaElse.hasReturnStatementSomewhere();
        if (rIf != null && rElse != null) {
            return rIf;
        } else {
            return null;
        }
    }
}
