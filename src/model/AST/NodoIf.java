package model.AST;

import model.AST.Expresiones.NodoExpresion;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import utils.exceptions.SemanticException;

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
}
