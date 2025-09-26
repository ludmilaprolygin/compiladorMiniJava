package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public class SymbolTable extends Element {
    private Table<Class> classes;
    private Table<Interface> interfaces;
    private static SymbolTable symbolTable;
    private MainElement current;

    private SymbolTable() {
        reset();
    }

    public static SymbolTable symbolTable() {
        if(symbolTable == null)
            symbolTable  = new SymbolTable();
        return symbolTable;
    }

    public void reset() {
        classes = new Table<>();
        interfaces = new Table<>();
        predefined();
    }

    public void setCurrentClass(MainElement c) {
        current = c;
    }

    public MainElement getCurrentClass() {
        return current;
    }

    public void addClass(Token t, Class c) throws SemanticException {
        if(!classes.contains(t.getLexeme()))
            classes.put(t, c);
        else {
            throw new SemanticException(SemanticErrorMessage.classAlreadyExists(t));
        }
    }

    public Table<Class> getClasses() { return classes; }
    public Table<Interface> getInterfaces() { return interfaces; }

    private void predefined() {
        createObject();
        createString();
        createSystem();
    }

    private void createObject() {
        Token t = new Token(null, "Object", -1);
        Class c = new Class(null, t, null);
        classes.put(t, c);
        //TODO - add debugPrint
    }

    private void createString() {
        Token t = new Token(null, "String", -1);
        Class c = new Class(null, t, null);
        classes.put(t, c);
    }

    private void createSystem() {
        Token t = new Token(null, "System", -1);
        Class c = new Class(null, t, null);
        classes.put(t, c);
        //TODO - add methods
    }
}
