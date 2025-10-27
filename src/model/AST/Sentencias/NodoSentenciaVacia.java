package model.AST.Sentencias;

import utils.exceptions.SemanticException;

public class NodoSentenciaVacia extends NodoSentencia {
    @Override
    public void check() throws SemanticException { }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + "{ nodo sentencia vacia }\n";
    }

    @Override
    protected void checkThisOnStaticContext() throws SemanticException {

    }


}
