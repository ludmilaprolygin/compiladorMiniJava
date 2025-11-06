package model.AST.Sentencias;

import model.AST.Expresiones.NodoExpresion;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class NodoForStandard extends NodoSentencia {
    protected NodoExpresion condicion;
    protected NodoSentencia asignacion;
    protected NodoSentencia incremento;

    public NodoForStandard(NodoExpresion condicion, NodoSentencia incremento, NodoSentencia asignacion) {
        this.condicion = condicion;
        this.asignacion = asignacion;
        this.incremento = incremento;
    }

    public NodoForStandard(NodoExpresion c){
        condicion = c;
        asignacion = new NodoSentenciaVacia();
        incremento = new NodoSentenciaVacia();
    }

    @Override
    public void check() throws SemanticException {
        AbstractType expType = condicion.check();
        expType.compatible(new BooleanType(null));

        asignacion.check();
        incremento.check();
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "forStandard\n";
        toReturn += asignacion.toString(depth + 1);
        toReturn += condicion.toString(depth + 1);
        toReturn += incremento.toString(depth + 1);
        return toReturn;
    }

    @Override
    protected void checkThisOnStaticContext() throws SemanticException {
        asignacion.checkThisOnStaticContext();
        incremento.checkThisOnStaticContext();
    }

    @Override
    public void gen(OutputManager o) {

    }
}
