package model.AST;

import utils.exceptions.SemanticException;

import java.util.LinkedList;
import java.util.List;

public class NodoBloque extends NodoSentencia {

    protected final List<NodoSentencia> statements;

    public NodoBloque() {
        statements = new LinkedList<>();
    }

    public List<NodoSentencia> getStatements() { return statements; }

    public void addStatement (NodoSentencia statement) {
        statements.add(statement);
    }
    public void check() throws SemanticException {
        //TODO - complete
    }
}
