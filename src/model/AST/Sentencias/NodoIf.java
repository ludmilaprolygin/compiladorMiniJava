package model.AST.Sentencias;

import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoThis;
import model.codeGeneration.Instructions;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import outputManager.OutputManager;
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
    public void checkThisOnStaticContext() throws SemanticException {
        if(condicion instanceof NodoThis){
            throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(condicion.getToken()));
        }
        sentenciaIf.checkThisOnStaticContext();
        sentenciaElse.checkThisOnStaticContext();
    }

    public NodoReturn hasReturnStatementSomewhere() throws SemanticException {
        NodoReturn rIf = sentenciaIf.hasReturnStatementSomewhere();
        NodoReturn rElse = sentenciaElse.hasReturnStatementSomewhere();
        if (rIf != null && rElse != null) {
            return rElse;
        } else {
            return null;
        }
    }

    @Override
    public void gen(OutputManager o) {
        String lblElse = "lbl_else@"+condicion.getToken().getRow();
        String lblFin = "lbl_fin_if@"+condicion.getToken().getRow();

        condicion.gen(o); //deja 0 o 1 en el tope
        o.gen(Instructions.BF + " " + lblElse);
        sentenciaIf.gen(o);
        o.gen(Instructions.JUMP + " " + lblFin);
        o.gen(lblElse + ": " + Instructions.NOP);

        if(!(sentenciaElse instanceof NodoSentenciaVacia)){
            sentenciaElse.gen(o);
        }
        o.gen(lblFin + ": " + Instructions.NOP);
    }
}
