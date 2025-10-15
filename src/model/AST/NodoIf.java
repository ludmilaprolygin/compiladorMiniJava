package model.AST;

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
        //TODO - completar
        AbstractType expType = condicion.check();
        expType.compatible(new BooleanType(null));

        sentenciaIf.check();
        sentenciaElse.check();
    }
}
