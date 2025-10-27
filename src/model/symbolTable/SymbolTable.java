package model.symbolTable;

import model.AST.Sentencias.NodoBloque;
import model.AST.Sentencias.NodoBloqueVacio;
import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;
import utils.messages.SemanticErrorIMessage;

import java.util.List;

import static model.TokenType.*;

public class SymbolTable extends Element {
    private Table<Class> classes;
    private Table<Interface> interfaces;
    private static SymbolTable symbolTable;
    private MainElement currentClass;
    private Service currentService;
    private HierarchyTree classHierarchy;
    private Class objectClass;
    private NodoBloque bloque;
    private boolean hasMain;

    private SymbolTable() {
        reset();
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        for(Class c : classes.values()){
            setCurrentClass(c);
            c.correctDeclaration();
        }
        for(Interface i : interfaces.values()) {
            setCurrentClass(i);
            i.correctDeclaration();
        }
    }

    public static SymbolTable symbolTable() {
        if(symbolTable == null)
            symbolTable  = new SymbolTable();
        return symbolTable;
    }

    public void reset() {
        classes = new Table<>();
        interfaces = new Table<>();
        bloque = new NodoBloqueVacio();
        hasMain = false;
        predefined();
    }

    public void setCurrentClass(MainElement c) {
        currentClass = c;
    }

    public boolean hasMain() { return hasMain; }
    public void setHasMain() { hasMain = true; }

    public MainElement getCurrentClass() {
        return currentClass;
    }

    public void setCurrentService(Service s) {
        currentService = s;
    }

    public Service getCurrentService() {
        return currentService;
    }

    public void setBloque(NodoBloque b) { bloque = b; }
    public NodoBloque getBloque() { return bloque; }

    public void addClass(Token t, Class c) throws SemanticException {
        if(interfaces.contains(t.getLexeme()))
            throw new SemanticException(SemanticErrorIMessage.interfaceAlreadyExists(t));
        else if(!classes.contains(t.getLexeme())) {
            classes.put(t, c);
            HierarchyTree ht = classHierarchy.search(c.getInheritance().getLexeme());
            if(ht != null)
                ht.addDescendant(new HierarchyTree(t.getLexeme()));
            else {
                HierarchyTree parent = new HierarchyTree(c.getInheritance().getLexeme());
                classHierarchy.addDescendant(parent);
                parent.addDescendant(new HierarchyTree(t.getLexeme()));
            }
        }
        else {
            throw new SemanticException(SemanticErrorIMessage.classAlreadyExists(t));
        }
    }

    public void addInterface(Token t, Interface i) throws SemanticException {
        if(classes.contains(t.getLexeme()))
            throw new SemanticException(SemanticErrorIMessage.classAlreadyExists(t));
        else if(!interfaces.contains(t.getLexeme())){
            interfaces.put(t, i);
            if(i.getInheritance() != null) {
                HierarchyTree ht = classHierarchy.search(i.getInheritance().getLexeme());
                if (ht != null)
                    ht.addDescendant(new HierarchyTree(t.getLexeme()));
                else {
                    HierarchyTree parent = new HierarchyTree(i.getInheritance().getLexeme());
                    classHierarchy.addDescendant(parent);
                    parent.addDescendant(new HierarchyTree(t.getLexeme()));
                }
            }
        }
        else {
            throw new SemanticException(SemanticErrorIMessage.interfaceAlreadyExists(t));
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
        objectClass = c;
        classes.put(tk, c);
        classHierarchy = new HierarchyTree(tk.getLexeme());

        Token n, v, m;
        AbstractType t;
        n = new Token(idMetVar, "debugPrint", -1);
        v = new Token(reservedPublic, "public", -1);
        m = new Token(reservedStatic, "static", -1);
        t = new VoidType(new Token(reservedVoid, "void", -1));
        AbstractType tInt = new IntType(new Token(reservedInt, "int", -1));

        Method debugPrint = new Method(n, v, m, t);
        Token pI = new Token(idMetVar, "i", -1);
        try {
            debugPrint.addParameter(new Parameter(pI, tInt));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, debugPrint);
        debugPrint.setBloque(new NodoBloqueVacio());
        debugPrint.pass();
    }

    private void createString() {
        Token t = new Token(null, "String", -1);

        Token tParent = classes.getTokenByName("Object");
        Class c = new Class(null, t, null, objectClass);
        classes.put(t, c);
        classHierarchy.search(tParent.getLexeme()).addDescendant(new HierarchyTree(t.getLexeme()));
    }

    private void createSystem() {
        Token t = new Token(null, "System", -1);

        Token tParent = classes.getTokenByName("Object");
        Class c = new Class(null, t, null, objectClass);

        classes.put(t, c);
        classHierarchy.search(tParent.getLexeme()).addDescendant(new HierarchyTree(t.getLexeme()));

        systemMethods(c);
    }

    private void systemMethods(Class c) {
        Token n;
        Token p = new Token(reservedPublic, "public", -1);
        Token s = new Token(reservedStatic, "static", -1);
        AbstractType v = new VoidType(new Token(reservedVoid, "void", -1));
        AbstractType i = new IntType(new Token(reservedInt, "int", -1));

        AbstractType tBoolean = new BooleanType(new Token(reservedBoolean, "boolean", -1));
        AbstractType tChar = new CharType(new Token(reservedChar,  "char", -1));
        AbstractType tString = new ClassType(new Token(idClase, "String", -1));
        AbstractType tInt = new IntType(new Token(reservedInt, "int", -1));

        n = new Token(idMetVar, "read", -1);
        Method read = new Method(n, p, s, i);
        c.getMethods().put(n, read);
        read.setBloque(new NodoBloqueVacio());
        read.pass();

        n = new Token(idMetVar, "printB", -1);
        Method printB = new Method(n, p, s, v);
        Token pB = new Token(idMetVar, "b", -1);
        try {
            printB.addParameter(new Parameter(pB, tBoolean));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printB);
        printB.setBloque(new NodoBloqueVacio());
        printB.pass();

        n = new Token(idMetVar, "printC", -1);
        Method printC = new Method(n, p, s, v);
        Token pC = new Token(idMetVar, "c", -1);
        try {
            printC.addParameter(new Parameter(pC, tChar));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printC);
        printC.setBloque(new NodoBloqueVacio());
        printC.pass();

        n = new Token(idMetVar, "printI", -1);
        Method printI = new Method(n, p, s, v);
        Token pI = new Token(idMetVar, "i", -1);
        try {
            printI.addParameter(new Parameter(pI, tInt));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printI);
        printI.setBloque(new NodoBloqueVacio());
        printI.pass();

        n = new Token(idMetVar, "printS", -1);
        Method printS = new Method(n, p, s, v);
        Token pS = new Token(idMetVar, "s", -1);
        try {
            printS.addParameter(new Parameter(pS, tString));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printS);
        printS.setBloque(new NodoBloqueVacio());
        printS.pass();

        n = new Token(idMetVar, "println", -1);
        Method println = new Method(n, p, s, v);
        c.getMethods().put(n, println);
        println.setBloque(new NodoBloqueVacio());
        println.pass();

        n = new Token(idMetVar, "printBln", -1);
        Method printBln = new Method(n, p, s, v);
        try {
            printBln.addParameter(new Parameter(pB, tBoolean));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printBln);
        printBln.setBloque(new NodoBloqueVacio());
        printBln.pass();

        n = new Token(idMetVar, "printCln", -1);
        Method printCln = new Method(n, p, s, v);
        try {
            printCln.addParameter(new Parameter(pC, tChar));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printCln);
        printCln.setBloque(new NodoBloqueVacio());
        printCln.pass();

        n = new Token(idMetVar, "printIln", -1);
        Method printIln = new Method(n, p, s, v);
        try {
            printIln.addParameter(new Parameter(pI, tInt));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printIln);
        printIln.setBloque(new NodoBloqueVacio());
        printIln.pass();

        n = new Token(idMetVar, "printSln", -1);
        Method printSln = new Method(n, p, s, v);
        try {
            printSln.addParameter(new Parameter(pS, tString));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printSln);
        printSln.setBloque(new NodoBloqueVacio());
        printSln.pass();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Class c : classes.values()) {
            sb.append(c.toString()).append("\n");
        }
        for (Interface i : interfaces.values()) {
            sb.append(i.toString()).append("\n");
        }
        sb.append(classHierarchy.toString()).append("\n");

        return sb.toString();
    }

    public void consolidate() throws SemanticException {
        List<String> sortedClasses = classHierarchy.getClassesByDepth();
        for(String s : sortedClasses) {
            Token t = classes.getTokenByName(s);
            Class c = classes.get(t);
            if(c != null && c.getInheritance() != null)
                c.consolidate();
        }
        for(Interface i : interfaces.values())
            if(i.getInheritance() != null){
                i.consolidate();
            }
    }

    public HierarchyTree getClassHierarchy() { return classHierarchy; }
    public Class getObjectClass () { return objectClass; }

    public void check() throws SemanticException {
        List<String> sortedClasses = classHierarchy.getClassesByDepth();
        for(String s : sortedClasses) {
            Token t = classes.getTokenByName(s);
            Class c = classes.get(t);
            if(c != null && c.getInheritance() != null)
            {
                setCurrentClass(c);
                c.check();
            }
        }
        if(hasMain == false)
            throw new SemanticException(SemanticErrorIIMessage.missingMainMethod());
        for(Interface i : interfaces.values())
            if(i.getInheritance() != null){
                {
                    setCurrentClass(i);
                    i.check();
                }
            }
    }
}
