package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class Attribute extends Element implements Var {
    private AbstractType type;
    public Attribute(Token n, AbstractType t) {
        super(n);
        type = t;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        type.correctDeclaration();
    }

    public AbstractType getType() { return type; }

    public String toString() {
        return type.toString() + " " + super.toString();
    }
}
