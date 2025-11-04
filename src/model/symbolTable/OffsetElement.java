package model.symbolTable;

import model.Token;

public abstract class OffsetElement extends Element{
    public OffsetElement() { super(); }

    public OffsetElement(Token n) {
        super(n);
    }

    public abstract int getOffset();
}
