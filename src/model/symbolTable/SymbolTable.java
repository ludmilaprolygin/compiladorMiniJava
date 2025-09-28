package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.TokenType.*;

public class SymbolTable extends Element {
    private Table<Class> classes;
    private Table<Interface> interfaces;
    private static SymbolTable symbolTable;
    private MainElement currentClass;
    private Method currentMethod;

    private SymbolTable() {
        reset();
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        for(Class c : classes.values())
            c.correctDeclaration();
        for(Interface i : interfaces.values())
            i.correctDeclaration();
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
        currentClass = c;
    }

    public MainElement getCurrentClass() {
        return currentClass;
    }

    public void setCurrentMethod(Method m) {
        currentMethod = m;
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }

    public void addClass(Token t, Class c) throws SemanticException {
        if(interfaces.contains(t.getLexeme()))
            throw new SemanticException(SemanticErrorMessage.interfaceAlreadyExists(t));
        else if(!classes.contains(t.getLexeme()))
            classes.put(t, c);
        else {
            throw new SemanticException(SemanticErrorMessage.classAlreadyExists(t));
        }
    }

    public void addInterface(Token t, Interface i) throws SemanticException {
        if(classes.contains(t.getLexeme()))
            throw new SemanticException(SemanticErrorMessage.classAlreadyExists(t));
        else if(!interfaces.contains(t.getLexeme()))
            interfaces.put(t, i);
        else {
            throw new SemanticException(SemanticErrorMessage.interfaceAlreadyExists(t));
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
        Token tk = new Token(null, "Object", -1);
        Class c = new Class(null, tk, null, null);
        classes.put(tk, c);

        Token n, v, m;
        MethodType t;
        n = new Token(idMetVar, "debugPrint", -1);
        v = new Token(reservedPublic, "public", -1);
        m = new Token(reservedStatic, "static", -1);
        t = new MethodType(new Token(reservedVoid, "void", -1));

        Method debugPrint = new Method(n, v, m, t);
        c.getMethods().put(n, debugPrint);

        //TODO - add debugPrint parameters
    }

    private void createString() {
        Token t = new Token(null, "String", -1);

        Token tParent = classes.getTokenByName("Object");
        Class c = new Class(null, t, null, tParent);
        classes.put(t, c);
    }

    private void createSystem() {
        Token t = new Token(null, "System", -1);

        Token tParent = classes.getTokenByName("Object");
        Class c = new Class(null, t, null, tParent);
        classes.put(t, c);
        //TODO - add methods
    }

    public String toString() {
        String toReturn = "";
        for(Class c : classes.values())
            toReturn += c.toString() + "\n";
        for(Interface i : interfaces.values())
            toReturn += i.toString() + "\n";
        return toReturn;
    }
}
