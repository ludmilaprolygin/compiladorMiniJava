package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class NullType extends AbstractType{
    public NullType(Token n) {
        super(n);
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    public boolean compatible(AbstractType t) throws SemanticException {
        if(t.isPrimitive())
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(t.getName()));
        return true;
    }
}
