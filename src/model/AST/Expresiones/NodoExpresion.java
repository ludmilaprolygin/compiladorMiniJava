package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public abstract class NodoExpresion {
    public abstract AbstractType check() throws SemanticException;
    public abstract String toString(int depth);

    public abstract Token getToken();
    public abstract Encadenado getLastEncadenado();

    public AbstractType checkLeftValue() throws SemanticException {
        throw new SemanticException(
                SemanticErrorIIMessage.invalidLeftValue(this.getToken())
        );
    }
}
