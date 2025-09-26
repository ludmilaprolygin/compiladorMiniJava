package model.symbolTable;

import model.Token;

public class Method extends Element {
    private Token visibility;
    private Type returnType;
    private Table<Parameter> parameters;

    public Method (Token n, Token v, Type t) {
        super(n);
        visibility = v;
        returnType = t;
        parameters = new Table<>();
    }
}
