package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class Attribute extends Element implements Var {
    private AbstractType type;
    protected int offset;
    public Attribute(Token n, AbstractType t) {
        super(n);
        type = t;
        offset = -1;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        type.correctDeclaration();
    }

    public AbstractType getType() { return type; }

    public String toString() {
        return "Offset: " + offset + " " + type.toString() + " " + super.toString();
    }

    public void setOffset(int offset) { this.offset = offset;}
    public int getOffset() { return this.offset; }
}
