package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Class extends MainElement {
    private Table<Constructor> constructors;
    public Class(Token m, Token n, Type t, Token i) {
        super(m, n, t, i);
        constructors = new Table<>();
    }

    public void addConstructor(Token t, Service s) throws SemanticException {
        Constructor c = (Constructor) s;
        if(!constructors.contains(t.getLexeme()))
            constructors.put(t, c);
        else {
            throw new SemanticException(SemanticErrorMessage.constructorAlreadyExists(t));
        }
    }

    public void addMethod(Token t, Service s) throws SemanticException {
        Method m = (Method) s;
        Token tParent = inheritance != null ? symbolTable().getClasses().getTokenByName(inheritance.getLexeme()) : null;
        Class cParent = symbolTable().getClasses().get(tParent);
        for(Method mParent: cParent.getMethods().values())
            if(mParent.equalSignature(m))
                throw new SemanticException(SemanticErrorMessage.methodDoesNotOverrideCorrectly(m));
        super.addMethod(t, m);
    }

    public Table<Constructor> getConstructors() { return constructors; }

    @Override
    public void correctDeclaration() throws SemanticException {
        String parentLexeme = inheritance != null ? inheritance.getLexeme() : null;
        if(inheritance != null && !symbolTable().getClasses().contains(parentLexeme)) {
            throw new SemanticException(SemanticErrorMessage.parentDoesNotExist(inheritance));
        }
        else if(inheritance != null && parentLexeme.equals(name.getLexeme())) {
            throw new SemanticException(SemanticErrorMessage.circularHierarchy(inheritance));
        }
        else if (inheritance != null && symbolTable().getClassHierarchy().isAncestor(name.getLexeme(), parentLexeme)) {
            throw new SemanticException(SemanticErrorMessage.circularHierarchy(inheritance));
        }
        for (Attribute a : attributes.values())
            a.correctDeclaration();
        for (Method m : methods.values()) {
            m.correctDeclaration();
            if(modifier != null && m.getModifier() != null &&
                !modifier.getTokenType().equals(TokenType.reservedAbstract) &&
                m.getModifier().getTokenType().equals(TokenType.reservedAbstract)) {
                    throw new SemanticException(SemanticErrorMessage.cannotDeclareAbstractMethod(m.getName()));
            }
            else if (modifier == null && m.getModifier() != null &&
                    m.getModifier().getTokenType().equals(TokenType.reservedAbstract))
                throw new SemanticException(SemanticErrorMessage.cannotDeclareAbstractMethod(m.getName()));
        }
        if(modifier != null && modifier.getTokenType().equals(TokenType.reservedAbstract) && !constructors.isEmpty()) {
            Constructor c = constructors.values().iterator().next();
            throw new SemanticException(SemanticErrorMessage.constructorFoundInAbstractClass(c.getName()));
        }
        else
            for (Constructor c : constructors.values())
                c.correctDeclaration();
        addPredefinedBuilder();
    }

    public String toString() {
        return super.toString() + "\n" +
                "   Constructors: " + constructors.toString() + "\n";
    }

    public void consolidate() throws SemanticException {
        if(inheritance != null){
            String parentLexeme = inheritance.getLexeme();
            Token tParent = symbolTable().getClasses().getTokenByName(parentLexeme);
            Class cParent = symbolTable().getClasses().get(tParent);
            Table<Method> parentMethods = cParent.getMethods();
            for(Method m : methods.values()){
                if(parentMethods.contains(m.getName().getLexeme())){
                    Token tkParent = parentMethods.getTokenByName(m.getName().getLexeme());
                    Method mParent = parentMethods.get(tkParent);
                    if(mParent != null)
                        System.out.println(m.getName().getLexeme() + " " + mParent.getName().getLexeme());
                    if(mParent != null && (!m.getReturnType().getName().getLexeme().equals(mParent.getReturnType().getName().getLexeme()) ||
                            m.getParameters().size() != mParent.getParameters().size())) {
                        throw new SemanticException(SemanticErrorMessage.methodDoesNotOverrideCorrectly(m));
                    }
                }
            }
            for(Method m : parentMethods.values()){
                if(!methods.contains(m.getName().getLexeme())){
                    Method mCopy = new Method(m.getName(), m.getVisibility(), m.getModifier(), m.getReturnType());
                    for(Parameter p : m.getParameters())
                        mCopy.addParameter(p);
                    methods.put(mCopy.getName(), mCopy);
                }
            }
        }
    }

    private void addPredefinedBuilder() {
        Token t = new Token(null, name.getLexeme(), -1);
        Constructor c = new Constructor(t, new Token(TokenType.reservedPublic, "public", -1));
        if(constructors.isEmpty()) {
            constructors.put(t, c);
        }
    }
}
