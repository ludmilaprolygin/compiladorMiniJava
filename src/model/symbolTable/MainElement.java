package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public abstract class MainElement extends Element {
    protected Token modifier;
    protected Token inheritance;
    protected AbstractType parametricType;
    protected List attributes;
    protected List methods;
    protected char inheritanceType;

    public MainElement(Token m, Token n, AbstractType t, Token i) {
        super(n);
        modifier = m;
        inheritance = i;
        parametricType = t;
        attributes = new List();
        methods = new List();
    }

    public void addAttribute(Token t, Attribute a) throws SemanticException {
        if (!attributes.contains(t.getLexeme()))
            attributes.put(t, a);
        else {
            throw new SemanticException(SemanticErrorIMessage.attributeAlreadyExists(t));
        }
    }

    public void addMethod(Token t, Method m) throws SemanticException {
        if(!methods.contains(t.getLexeme())){
            methods.put(t, m);
        }
        else {
            throw new SemanticException(SemanticErrorIMessage.methodAlreadyExists(t));
        }
    }

    public List getAttributes() {
        return attributes;
    }

    public List getMethods() {
        return methods;
    }

    public Token getModifier() {
        return modifier;
    }

    public Token getInheritance() {
        return inheritance;
    }

    public AbstractType getParametricType() {
        return parametricType;
    }
    public void setParametricType(AbstractType parametricType) {
        this.parametricType = parametricType;
    }

    public String toString() {
        String mod = (modifier != null) ? modifier.getLexeme() + " " : "";
        String inh = (inheritance != null) ? " extends " + inheritance.getLexeme() : "";
        String paramType = (parametricType != null) ? "<" + parametricType.getName().getLexeme() + ">" : "";
        return mod + name.getLexeme() + paramType + inh + " {\n" +
                "   Attributes: " + attributes.toString() + "\n" +
                "   Methods: " + methods.toString() + "\n";
    }

    public void correctDeclaration () throws SemanticException {
        if (modifier != null && modifier.getTokenType().equals(TokenType.reservedStatic)){
            throw new SemanticException(SemanticErrorIMessage.mainElementCannotBeStatic(modifier));
        }
    }

    public void consolidate() throws SemanticException {
        Table<Class> classes = symbolTable().getClasses();
        Token cParent = classes.getTokenByName(inheritance.getLexeme());
        if(cParent != null){
            for(Element m : classes.get(cParent).getMethods()) {
                if(methods.contains(m.getName().getLexeme())) {
                    Token thisToken = methods.getTokenByName(m.getName().getLexeme());
                    Method thisMethod = (Method) methods.get(thisToken);
                    if(thisMethod != null && thisMethod.equalSignature((Method) m)) {
                        throw new SemanticException(SemanticErrorIMessage.methodDoesNotOverrideCorrectly(thisMethod));
                    }
                }
            }
        }
    }

    protected boolean isAbstract() { return modifier != null && modifier.getTokenType().equals(TokenType.reservedAbstract); }
    protected boolean isFinal() { return modifier != null && modifier.getTokenType().equals(TokenType.reservedFinal); }
    protected boolean isStatic() { return modifier != null && modifier.getTokenType().equals(TokenType.reservedStatic); }

    public void check() throws SemanticException {
        for(Element m : methods)
        {
            symbolTable().setCurrentService((Service) m);
            ((Service) m).check();
        }
    }
}