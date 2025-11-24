package model.symbolTable;

import model.AST.Sentencias.Bloques.*;
import model.Token;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;
import utils.messages.SemanticErrorIMessage;

import java.util.ArrayList;
import java.util.List;

import static model.TokenType.*;

public class SymbolTable extends Element {
    private Table<Class> classes;
    private Table<Interface> interfaces;
    private static SymbolTable symbolTable;
    private MainElement currentClass;
    private Service currentService;
    private HierarchyTree classHierarchy;
    private ObjectClass objectClass;
    private NodoBloque bloque;
    private boolean hasMain;
    private MainElement mainClass;
    private List<MainElement> checkedElements;


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
        checkedElements = new ArrayList<>();
        createObject();
        createString();
        createSystem();
    }

    public void setCurrentClass(MainElement c) {
        currentClass = c;
    }

    public boolean hasMain() { return hasMain; }
    public void setHasMain(MainElement e) {
        hasMain = true;
        mainClass = e;
    }

    public MainElement getMainClass() { return mainClass; }

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

    private void createObject() {
        Token tk = new Token(null, "Object", -1);
        objectClass = new ObjectClass(null, tk, null, null);
        //objectClass = c;
        classes.put(tk, objectClass);
        classHierarchy = new HierarchyTree(tk.getLexeme());

        Token n, v, m;
        AbstractType t;
        n = new Token(idMetVar, "debugPrint", -1);
        v = new Token(reservedPublic, "public", -1);
        m = new Token(reservedStatic, "static", -1);
        t = new VoidType(new Token(reservedVoid, "void", -1));
        AbstractType tInt = new IntType(new Token(reservedInt, "int", -1));

        Method debugPrint = new Method(n, v, m, t, objectClass);
        Token pI = new Token(idMetVar, "i", -1);
        try {
            debugPrint.addParameter(new Parameter(pI, tInt));
        } catch (SemanticException e) {
        }
        ;
        objectClass.getMethods().put(n, debugPrint);
        debugPrint.setBloque(new NodoBloqueVacio());
        debugPrint.pass();
        debugPrint.setOffset(0);
        debugPrint.setBloque(new NodoBloqueDebugPrint());

        Token toStringTK = new Token(idMetVar, "toString", -1);
        AbstractType tVoid = new VoidType(new Token(reservedVoid, "void", -1));
        Method toString = new Method(toStringTK, v, null, tVoid, objectClass);
        objectClass.getMethods().put(toStringTK, toString);
        toString.setBloque(new NodoBloqueToString());
        toString.pass();
        debugPrint.setOffset(1);
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
        Method read = new Method(n, p, s, i, c);
        c.getMethods().put(n, read);
        read.setBloque(new NodoBloqueRead());
        read.pass();

        n = new Token(idMetVar, "printB", -1);
        Method printB = new Method(n, p, s, v, c);
        Token pB = new Token(idMetVar, "b", -1);
        try {
            printB.addParameter(new Parameter(pB, tBoolean));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printB);
        printB.setBloque(new NodoBloquePrintB());
        printB.pass();

        n = new Token(idMetVar, "printC", -1);
        Method printC = new Method(n, p, s, v, c);
        Token pC = new Token(idMetVar, "c", -1);
        try {
            printC.addParameter(new Parameter(pC, tChar));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printC);
        printC.setBloque(new NodoBloquePrintC());
        printC.pass();

        n = new Token(idMetVar, "printI", -1);
        Method printI = new Method(n, p, s, v, c);
        Token pI = new Token(idMetVar, "i", -1);
        try {
            printI.addParameter(new Parameter(pI, tInt));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printI);
        printI.setBloque(new NodoBloquePrintI());
        printI.pass();

        n = new Token(idMetVar, "printS", -1);
        Method printS = new Method(n, p, s, v, c);
        Token pS = new Token(idMetVar, "s", -1);
        try {
            printS.addParameter(new Parameter(pS, tString));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printS);
        printS.setBloque(new NodoBloquePrintS());
        printS.pass();

        n = new Token(idMetVar, "println", -1);
        Method println = new Method(n, p, s, v, c);
        c.getMethods().put(n, println);
        println.setBloque(new NodoBloquePrintln());
        println.pass();

        n = new Token(idMetVar, "printBln", -1);
        Method printBln = new Method(n, p, s, v, c);
        try {
            printBln.addParameter(new Parameter(pB, tBoolean));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printBln);
        printBln.setBloque(new NodoBloquePrintBln());
        printBln.pass();

        n = new Token(idMetVar, "printCln", -1);
        Method printCln = new Method(n, p, s, v, c);
        try {
            printCln.addParameter(new Parameter(pC, tChar));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printCln);
        printCln.setBloque(new NodoBloquePrintCln());
        printCln.pass();

        n = new Token(idMetVar, "printIln", -1);
        Method printIln = new Method(n, p, s, v, c);
        try {
            printIln.addParameter(new Parameter(pI, tInt));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printIln);
        printIln.setBloque(new NodoBloquePrintIln());
        printIln.pass();

        n = new Token(idMetVar, "printSln", -1);
        Method printSln = new Method(n, p, s, v, c);
        try {
            printSln.addParameter(new Parameter(pS, tString));
        }
        catch(SemanticException e) {};
        c.getMethods().put(n, printSln);
        printSln.setBloque(new NodoBloquePrintSln());
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

    public void gen(OutputManager outputManager) throws GenerationException {
        //objectClass.gen(outputManager);

        for(Class c : classes.values()){
            c.sort();
        }

        for(Class c : classes.values()){
            c.gen(outputManager);
        }
    }

    public void addCheckedClass(Token className, Class c) {
        checkedElements.add(c);
    }

    public List<MainElement> getCheckedElements() {
        return checkedElements;
    }

    public Class getCheckedClass(Token t){
        for(MainElement e : checkedElements){
            if(e.getName().getLexeme().equals(t.getLexeme()))
                return (Class)e;
        }
        return null;
    }
}
