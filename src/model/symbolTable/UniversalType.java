package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class UniversalType extends AbstractType {
    public UniversalType() {
        super(null);
    }

    public boolean compatible(AbstractType t) throws SemanticException { return true; }

    public String toString() {
        return "";
    }
}
