package model.AST;

import utils.exceptions.SemanticException;

public class NodoBloqueVacio extends NodoBloque {
    @Override
    public void check() throws SemanticException { }
}
