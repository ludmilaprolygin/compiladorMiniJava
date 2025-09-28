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

        systemMethods(c);
        //TODO - add parameters to methods
    }

    private void systemMethods(Class c) {
        Token n;
        Token p = new Token(reservedPublic, "public", -1);
        Token s = new Token(reservedStatic, "static", -1);
        MethodType v = new MethodType(new Token(reservedVoid, "void", -1));
        MethodType i = new MethodType(new Token(reservedInt, "int", -1));

        n = new Token(idMetVar, "read", -1);
        Method read = new Method(n, p, s, i);
        c.getMethods().put(n, read);

        n = new Token(idMetVar, "printB", -1);
        Method printB = new Method(n, p, s, v);
        c.getMethods().put(n, read);

        n = new Token(idMetVar, "printC", -1);
        Method printC = new Method(n, p, s, v);
        c.getMethods().put(n, printC);

        n = new Token(idMetVar, "printI", -1);
        Method printI = new Method(n, p, s, v);
        c.getMethods().put(n, printI);

        n = new Token(idMetVar, "printS", -1);
        Method printS = new Method(n, p, s, v);
        c.getMethods().put(n, printS);

        n = new Token(idMetVar, "println", -1);
        Method println = new Method(n, p, s, v);
        c.getMethods().put(n, println);

        n = new Token(idMetVar, "printBln", -1);
        Method printBln = new Method(n, p, s, v);
        c.getMethods().put(n, printBln);

        n = new Token(idMetVar, "printCln", -1);
        Method printCln = new Method(n, p, s, v);
        c.getMethods().put(n, printCln);

        n = new Token(idMetVar, "printIln", -1);
        Method printIln = new Method(n, p, s, v);
        c.getMethods().put(n, printIln);

        n = new Token(idMetVar, "printSln", -1);
        Method printSln = new Method(n, p, s, v);
        c.getMethods().put(n, printSln);
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
