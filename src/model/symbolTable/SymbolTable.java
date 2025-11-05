package model.symbolTable;

import model.AST.Expresiones.NodoExpresionAsignacion;
import model.AST.Operandos.NodoVar;
import model.AST.Sentencias.NodoBloque;
import model.AST.Sentencias.NodoBloqueVacio;
import model.AST.Sentencias.NodoReturn;
import model.AST.Sentencias.NodoSentenciaConExpresion;
import model.Token;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;
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
    private ObjectClass objectClass;
    private NodoBloque bloque;
    private boolean hasMain;
    private MainElement mainClass;

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
        createObject();
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
        }
        catch(SemanticException e) {};
        objectClass.getMethods().put(n, debugPrint);
        debugPrint.setBloque(new NodoBloqueVacio());
        debugPrint.pass();
        debugPrint.setOffset(0);
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
            c.gen(outputManager);
        }
    }
}
