package model.AST.Sentencias;

import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public abstract class NodoSentencia {
    public abstract void check() throws SemanticException;
    public abstract String toString(int depth);

    protected abstract void checkThisOnStaticContext() throws SemanticException;

    public NodoReturn hasReturnStatementSomewhere() throws SemanticException {
        return null;
    }

    public abstract void gen(OutputManager o);
}
