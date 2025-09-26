package model.symbolTable;

import model.Token;

public abstract class Element {
    protected Token name;

    public Element() {}

    public Element(Token n) {
        name = n;
    }
}
