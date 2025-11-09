package model.AST.Sentencias.Bloques;

import model.AST.Sentencias.NodoSentencia;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class NodoBloqueVacio extends NodoBloque {
    public NodoBloqueVacio(){
        super(null);
    }
    public NodoBloqueVacio(NodoBloque bc) {
        super(bc);
    }

    @Override
    public void check() throws SemanticException { }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + "{ nodo bloque vacio }\n";
    }

    public void checkThisOnStaticContext() throws SemanticException {}

    @Override
    public void gen(OutputManager o) { }
}
