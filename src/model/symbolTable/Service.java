package model.symbolTable;

import model.Token;

public abstract class Service extends Element {
    protected Token visibility;
    protected Table<Parameter> parameters;
    public Service(Token n, Token v) {
        super(n);
        visibility = v;
        parameters = new Table<>();
    }
}
