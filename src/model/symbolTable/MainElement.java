package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public abstract class MainElement extends Element {
    protected Token modifier;
    protected Token inheritance;
    protected Type parametricType;
    protected Table<Attribute> attributes;
    protected Table<Method> methods;

    public MainElement(Token m, Token n, Type t, Token i) {
        super(n);
        modifier = m;
        inheritance = i;
        parametricType = t;
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

    public Table<Attribute> getAttributes() {
        return attributes;
    }

    public Table<Method> getMethods() {
        return methods;
    }

    public Token getModifier() {
        return modifier;
    }

    public Token getInheritance() {
        return inheritance;
    }

    public String toString() {
        String mod = (modifier != null) ? modifier.getLexeme() + " " : "";
        String inh = (inheritance != null) ? " extends " + inheritance.getLexeme() : "";
        String paramType = (parametricType != null) ? "<" + parametricType.getName().getLexeme() + ">" : "";
        return mod + name.getLexeme() + paramType + inh + " {\n" +
                "   Attributes: " + attributes.toString() + "\n" +
                "   Methods: " + methods.toString() + "\n" +
                "}";
    }
}
