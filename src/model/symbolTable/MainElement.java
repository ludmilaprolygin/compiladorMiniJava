package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public abstract class MainElement extends Element {
    private Token modifier;
    private Token name;
    private Token inheritance;
    private Table<Attribute> attributes;
    private Table<Method> methods;

    public MainElement(Token m, Token n, Token i) {
        super(n);
        modifier = m;
        inheritance = i;
        attributes = new Table<>();
        methods = new Table<>();
    }

    public void addAttribute(Token t, Attribute a) throws SemanticException {
        if (!attributes.contains(t.getLexeme()))
            attributes.put(t, a);
        else {
            throw new SemanticException(SemanticErrorMessage.attributeAlreadyExists(t));
        }
    }

    public void addMethod(Token t, Method m) throws SemanticException {
        if(!methods.contains(t.getLexeme()))
            methods.put(t, m);
        else {
            throw new SemanticException(SemanticErrorMessage.methodAlreadyExists(t));
        }
    }

    public Token getModifier() {
        return modifier;
    }

    public Token getName() {
        return name;
    }

    public Token getInheritance() {
        return inheritance;
    }
}
