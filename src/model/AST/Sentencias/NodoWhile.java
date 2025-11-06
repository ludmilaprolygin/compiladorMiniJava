package model.AST.Sentencias;

import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoThis;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoWhile extends NodoSentencia {
    protected NodoExpresion condicion;
    protected NodoSentencia sentencia;

    public NodoWhile(NodoExpresion c, NodoSentencia s){
        condicion = c;
        sentencia = s;
    }

    @Override
    public void check() throws SemanticException {
        AbstractType expType = condicion.check();
        expType.compatible(new BooleanType(null));

        sentencia.check();
    }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "while\n";
        toReturn += condicion.toString(depth + 1);
        toReturn += sentencia.toString(depth + 1);
        return toReturn;
    }

    @Override
    protected void checkThisOnStaticContext() throws SemanticException {
        if(condicion instanceof NodoThis){
            throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(condicion.getToken()));
        }
        sentencia.checkThisOnStaticContext();
    }

    public NodoReturn hasReturnStatementSomewhere() throws SemanticException {
        return sentencia.hasReturnStatementSomewhere();
    }

    @Override
    public void gen(OutputManager o) {

    }
}
