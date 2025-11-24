package model.symbolTable;

import model.Token;

public abstract class OffsetElement extends Element{
    public OffsetElement() { super(); }

    public OffsetElement(Token n) {
        super(n);
    }

    public abstract int getOffset();

    public abstract Token getModifier();

    public void setOffset(int o){}
    public abstract AbstractType getType();

    public String getMnemonic() { return ""; }
    public void setMnemonic(String m) { }
}
