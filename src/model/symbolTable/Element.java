package model.symbolTable;

import model.Firsts;
import model.Token;
import utils.exceptions.SemanticException;

public abstract class Element {
    protected Token name;

    public Element() {}

    public Element(Token n) {
        name = n;
    }

    public Token getName() {
        return name;
    }

    public abstract void correctDeclaration() throws SemanticException;

    public String toString() {
        return name.getLexeme();
    }
}
