package model.symbolTable;

import model.Token;

public class CharType extends AbstractType {
    public CharType(Token n) {
        super(n);
    }
    @Override
    public boolean isPrimitive() {
        return true;
    }
}
