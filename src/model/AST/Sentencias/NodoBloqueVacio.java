package model.AST.Sentencias;

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
}
