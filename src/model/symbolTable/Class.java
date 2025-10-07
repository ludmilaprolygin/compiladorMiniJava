package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Class extends MainElement {
    private List builderTable;
    private MainElement parentClass;
    public Class(Token m, Token n, AbstractType t, MainElement i) {
        super(m, n, t, (i != null ? i.getName() : null));
        parentClass = i;
        builderTable = new List();
    }

    public void addConstructor(Token t, Service s) throws SemanticException {
        Builder c = (Builder) s;
        if(!builderTable.contains(t.getLexeme()))
            builderTable.addLast(c);
        else {
            throw new SemanticException(SemanticErrorMessage.constructorAlreadyExists(t));
        }
    }

    public void addMethod(Token t, Service s) throws SemanticException {
        Method m = (Method) s;
        Token tParent = inheritance != null ? symbolTable().getClasses().getTokenByName(inheritance.getLexeme()) : null;
        Class cParent = symbolTable().getClasses().get(tParent);
        for(Method mParent: cParent.getMethods().values())
            if(mParent.getName().getLexeme().equals(m.getName().getLexeme()) &&
                    !mParent.equalSignature(m) &&
                    mParent.getModifier() != null && !mParent.getModifier().getTokenType().equals(TokenType.reservedAbstract))
                throw new SemanticException(SemanticErrorMessage.methodDoesNotOverrideCorrectly(m));
        super.addMethod(t, m);
    }

    public List getBuilderTable() { return builderTable; }

    @Override
    public void correctDeclaration() throws SemanticException {
        super.correctDeclaration();
        String parentLexeme = inheritance != null ? inheritance.getLexeme() : null;
        if(inheritance != null) {
            correctInheritance(parentLexeme);
        }
        for (Attribute a : attributes.values())
            if((parametricType == null) || (!a.getType().equals(parametricType)))
                a.correctDeclaration();
        for (Method m : methods.values()) {
            correctMethod(m);
        }
        correctBuilder();
    }

    public String toString() {
        return super.toString() + "\n" +
                "   Constructors: " + builderTable.toString() + "\n";
    }

    public void consolidate() throws SemanticException {
        super.consolidate();
        MainElement parent = getParent();
        if(inheritance != null){
            if (parent.getParametricType() != null && parametricType != null &&
                    parentClass.getParametricType() != null && !parentClass.getParametricType().getName().getLexeme().equals(parametricType.getName().getLexeme())) {
                throw new SemanticException(SemanticErrorMessage.parametricInheritanceMismatch(parametricType.getName()));
            }
            else if(parent.getParametricType() != null && parametricType == null) {
                throw new SemanticException(SemanticErrorMessage.parametricInheritanceMismatch(name));
            }

            if(parent instanceof Class cParent) { // extends
                if ((parentClass.getParametricType() != null && cParent.getParametricType() == null) || (parentClass.getParametricType() == null && cParent.getParametricType() != null))
                    throw new SemanticException(SemanticErrorMessage.parametricInheritanceMismatch(parentClass.getName()));

                Table<Method> parentMethods = cParent.getMethods();
                Table<Attribute> parentAttributes = cParent.getAttributes();
                for(Method m : methods.values()){
                    Token tkParent = parentMethods.getTokenByName(m.getName().getLexeme());
                    Method mParent = parentMethods.get(tkParent);
                    if(parentMethods.contains(m.getName().getLexeme())){
                        if(mParent != null && (!m.getReturnType().getName().getLexeme().equals(mParent.getReturnType().getName().getLexeme()) ||
                                m.getParameters().size() != mParent.getParameters().size())) {
                            throw new SemanticException(SemanticErrorMessage.methodDoesNotOverrideCorrectly(m));
                        }
                        if(mParent != null && mParent.getModifier() != null && mParent.getModifier().getTokenType().equals(TokenType.reservedAbstract) && m.getModifier() != null &&
                                m.getModifier().getTokenType().equals(TokenType.reservedAbstract) && !isAbstract()) {
                            throw new SemanticException(SemanticErrorMessage.missingImplementationOnAbstracMethod(m));
                        }
                        if(mParent != null && mParent.getModifier() != null && mParent.getModifier().getTokenType().equals(TokenType.reservedAbstract) && m.getModifier() != null &&
                                m.getModifier().getTokenType().equals(TokenType.reservedAbstract)) {
                            throw new SemanticException(SemanticErrorMessage.abstractMethodRedefinedAsAbstract(m));
                        }
                        if(mParent != null && mParent.getModifier() != null && mParent.getModifier().getTokenType().equals(TokenType.reservedFinal)) {
                            throw new SemanticException(SemanticErrorMessage.cannotOverrideFinalMethod(m));
                        }
                        if(mParent != null && !mParent.getParameters().equals(m.getParameters())) {
                            throw new SemanticException(SemanticErrorMessage.methodDoesNotOverrideCorrectly(m));
                        }
                    }
                }
                for(Method m : parentMethods.values()){
                    if(!methods.contains(m.getName().getLexeme()) && m.getModifier() != null && m.getModifier().getTokenType().equals(TokenType.reservedAbstract) && !isAbstract()){
                        throw new SemanticException(SemanticErrorMessage.cannotDeclareAbstractMethod(name));
                    }
                    if(!methods.contains(m.getName().getLexeme())){
                        Method mCopy = new Method(m.getName(), m.getVisibility(), m.getModifier(), m.getReturnType(), m.getEmptyBody());
                        for(Element p : m.getParameters())
                            mCopy.addParameter((Parameter) p);
                        methods.put(mCopy.getName(), mCopy);
                    }
                }
                List parentBuilder = cParent.getBuilderTable();
                Builder myBuilder = null;
                if(!builderTable.isEmpty())
                    myBuilder = (Builder) builderTable.getFirst();
                for(Element b : parentBuilder){
                    if(!((Builder)b).getParameters().isEmpty() && myBuilder != null && myBuilder.getParameters().isEmpty()){
                        throw new SemanticException(SemanticErrorMessage.builderDoesNotOverrideCorrectly(myBuilder));
                    }

                }
                /*
                for(Attribute a : attributes.values()){
                    if(parentAttributes.contains(a.getName().getLexeme())){
                        throw new SemanticException(SemanticErrorMessage.attributeAlreadyExists(a.getName()));
                    }
                }
                 */
                for(Attribute parentAttribute : parentAttributes.values()){
                    if(!attributes.contains(parentAttribute.getName().getLexeme())){
                        Attribute aCopy = new Attribute(parentAttribute.getName(), parentAttribute.getType());
                        attributes.put(aCopy.getName(), aCopy);
                    }
                }
            }
            else { // implements
                parent = getParentInterface();
                Interface iParent = (Interface) parent;
            }
        }
    }

    private void addPredefinedBuilder() {
        Token t = new Token(null, name.getLexeme(), -1);
        Builder c = new Builder(t, new Token(TokenType.reservedPublic, "public", -1));
        if(builderTable.isEmpty() && modifier != null && !modifier.getTokenType().equals(TokenType.reservedAbstract)) {
            builderTable.addLast(c);
        }
        else if(builderTable.isEmpty() && modifier == null){
            builderTable.addLast(c);
        }
    }
    private boolean objectIsParent() {
        return inheritance != null && inheritance.getLexeme().equals("Object");
    }
    private Class getParentClass() {
        Class cParent = null;
        if (inheritance != null) {
            String parentLexeme = inheritance.getLexeme();
            Token tParent = symbolTable().getClasses().getTokenByName(parentLexeme);
            cParent = symbolTable().getClasses().get(tParent);
        }
        return cParent;
    }
    private Interface getParentInterface(){
        Interface cParent = null;
        if (inheritance != null) {
            String parentLexeme = inheritance.getLexeme();
            Token tParent = symbolTable().getInterfaces().getTokenByName(parentLexeme);
            cParent = symbolTable().getInterfaces().get(tParent);
        }
        return cParent;
    }
    private MainElement getParent() throws SemanticException {
        Class c = getParentClass();
        Interface i = getParentInterface();
        return c == null ? i : c;
    }
    private void correctInheritance(String parentLexeme) throws SemanticException {
        if ((!symbolTable().getClasses().contains(parentLexeme) && !symbolTable().getInterfaces().contains(parentLexeme))) {
            throw new SemanticException(SemanticErrorMessage.parentDoesNotExist(inheritance));
        } else if (parentLexeme.equals(name.getLexeme())) {
            throw new SemanticException(SemanticErrorMessage.circularHierarchy(inheritance));
        } else if (symbolTable().getClassHierarchy().isAncestor(name.getLexeme(), parentLexeme)) {
            throw new SemanticException(SemanticErrorMessage.circularHierarchy(inheritance));
        }
        Class cParent = getParentClass();
        if (cParent != null) {
            if (cParent.isFinal()) {
                throw new SemanticException(SemanticErrorMessage.cannotExtendFromFinalClass(name));
            }
            if (this.isAbstract() && !cParent.isAbstract() && !objectIsParent()) {
                throw new SemanticException(SemanticErrorMessage.abstractClassExtendsConcreteClass(name));
            }
        }
    }
    private void correctMethod(Method m) throws SemanticException {
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
    private void correctBuilder() throws SemanticException {
        if(modifier != null && modifier.getTokenType().equals(TokenType.reservedAbstract) && !builderTable.isEmpty()) {
            Element c = builderTable.getFirst();
            throw new SemanticException(SemanticErrorMessage.builderFoundInAbstractClass(c.getName()));
        }
        else {
            for (Element c : builderTable) {
                c.correctDeclaration();
            }
        }
        addPredefinedBuilder();
    }
}
