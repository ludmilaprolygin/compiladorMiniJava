package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class UniversalType extends AbstractType {
    public UniversalType() {
        super(null);
    }

    public void compatible(AbstractType t) throws SemanticException { }
}
