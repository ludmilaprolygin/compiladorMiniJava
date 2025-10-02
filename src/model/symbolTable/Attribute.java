package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Attribute extends Element {
    private AbstractType type;
    public Attribute(Token n, AbstractType t) {
        super(n);
        type = t;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        type.correctDeclaration();
    }

    public String toString() {
        return type.toString() + " " + super.toString();
    }
}
