package model.symbolTable;

import model.AST.Operandos.NodoOperando;
import model.AST.Operandos.NodoVar;
import model.AST.Sentencias.Bloques.NodoBloqueVacio;
import model.Token;
import model.TokenType;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Class extends MainElement {
    private List builderTable;
    private MainElement parentClass;
    public Class(Token m, Token n, AbstractType t, MainElement i) {
        super(m, n, t, (i != null ? i.getName() : null));
        parentClass = i;
        builderTable = new List();
    }
    public Class(Token m, Token n, AbstractType t, MainElement i, char it) {
        super(m, n, t, (i != null ? i.getName() : null));
        parentClass = i;
        builderTable = new List();
        inheritanceType = it;
    }

    public void addConstructor(Token t, Service s) throws SemanticException {
        Builder c = (Builder) s;
        if(!builderTable.contains(t.getLexeme()))
            builderTable.addLast(c);
        else {
            throw new SemanticException(SemanticErrorIMessage.constructorAlreadyExists(t));
        }
    }

    public void addMethod(Token t, Service s) throws SemanticException {
        Method m = (Method) s;
        MainElement parent = getParent();
        for(Element mParent: parent.getMethods().values())
            if(mParent.getName().getLexeme().equals(m.getName().getLexeme()) &&
                    !((Method) mParent).equalSignature(m) &&
                    ((Method) mParent).getModifier() != null && !((Method) mParent).getModifier().getTokenType().equals(TokenType.reservedAbstract))
                throw new SemanticException(SemanticErrorIMessage.methodDoesNotOverrideCorrectly(m));
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
        for (Element a : attributes.values())
            if((parametricType == null) || (!((Attribute) a).getType().equals(parametricType)))
                a.correctDeclaration();
        for (Element m : methods.values()) {
            correctMethod((Method) m);
        }
        correctBuilder();
    }

    public String toString() {
        return super.toString() + "\n" +
                "   Builders: " + builderTable.toString() + "\n}";
    }

    public void consolidate() throws SemanticException {
        super.consolidate();
        MainElement parent = getParent();
        if (parent.getParametricType() != null && parametricType != null &&
                parentClass.getParametricType() != null && !parentClass.getParametricType().getName().getLexeme().equals(parametricType.getName().getLexeme())) {
            throw new SemanticException(SemanticErrorIMessage.parametricInheritanceMismatch(parametricType.getName()));
        }
        else if(parent.getParametricType() != null && parametricType == null) {
            throw new SemanticException(SemanticErrorIMessage.parametricInheritanceMismatch(name));
        }

        List parentAttributes = parent.getAttributes();
        consolidateAttributes(parentAttributes);

        List parentMethods = parent.getMethods();
        consolidateMethods(parentMethods);

        if(parent instanceof Class cParent) { // extends
            if ((parentClass.getParametricType() != null && cParent.getParametricType() == null) || (parentClass.getParametricType() == null && cParent.getParametricType() != null))
                throw new SemanticException(SemanticErrorIMessage.parametricInheritanceMismatch(parentClass.getName()));

            consolidateMethodsFromExtension(parentMethods);
            consolidateBuilder(cParent);

        }
        else { // implements
            consolidateMethodsFromImplementation(parentMethods);
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
            throw new SemanticException(SemanticErrorIMessage.parentDoesNotExist(inheritance));
        } else if (parentLexeme.equals(name.getLexeme())) {
            throw new SemanticException(SemanticErrorIMessage.circularHierarchy(inheritance));
        } else if (symbolTable().getClassHierarchy().isAncestor(name.getLexeme(), parentLexeme)) {
            throw new SemanticException(SemanticErrorIMessage.circularHierarchy(inheritance));
        } else if (inheritanceType == 'e' && symbolTable().getInterfaces().contains(parentLexeme)) {
            throw new SemanticException(SemanticErrorIMessage.classCannotExtendInterface(inheritance));
        } else if (inheritanceType == 'i' && symbolTable().getClasses().contains(parentLexeme)) {
            throw new SemanticException(SemanticErrorIMessage.classCannotImplementClass(inheritance));
        }
        Class cParent = getParentClass();
        if (cParent != null) {
            if (cParent.isFinal()) {
                throw new SemanticException(SemanticErrorIMessage.cannotExtendFromFinalClass(name));
            }
            if (this.isAbstract() && !cParent.isAbstract() && !objectIsParent()) {
                throw new SemanticException(SemanticErrorIMessage.abstractClassExtendsConcreteClass(name));
            }
        }
    }
    private void correctMethod(Method m) throws SemanticException {
        m.correctDeclaration();
        if(modifier != null && m.getModifier() != null &&
                !modifier.getTokenType().equals(TokenType.reservedAbstract) &&
                m.getModifier().getTokenType().equals(TokenType.reservedAbstract)) {
            throw new SemanticException(SemanticErrorIMessage.cannotDeclareAbstractMethod(m.getName()));
        }
        else if (modifier == null && m.getModifier() != null &&
                m.getModifier().getTokenType().equals(TokenType.reservedAbstract))
            throw new SemanticException(SemanticErrorIMessage.cannotDeclareAbstractMethod(m.getName()));
    }
    private void correctBuilder() throws SemanticException {
        if(modifier != null && modifier.getTokenType().equals(TokenType.reservedAbstract) && !builderTable.isEmpty()) {
            Element c = builderTable.getFirst();
            throw new SemanticException(SemanticErrorIMessage.builderFoundInAbstractClass(c.getName()));
        }
        else {
            for (Element c : builderTable) {
                c.correctDeclaration();
            }
        }
        addPredefinedBuilder();
    }
    private void consolidateAttributes(List parentAttributes) throws SemanticException {
        int max = -1;
        for(OffsetElement parentAttribute : parentAttributes.values()){
            Attribute a = (Attribute) parentAttribute;
            if(a.getOffset() > max){
                max = a.getOffset();
            }
            if(!attributes.contains(parentAttribute.getName().getLexeme())){
                Attribute aCopy = new Attribute(parentAttribute.getName(), ((Attribute) parentAttribute).getType());
                //attributes.put(aCopy.getName(), aCopy);

            }
            attributes.put(parentAttribute.getName(), parentAttribute);
        }
        /*
        for(Attribute a : attributes.values()){
            if(parentAttributes.contains(a.getName().getLexeme())){
                throw new SemanticException(SemanticErrorMessage.attributeAlreadyExists(a.getName()));
            }
        }
        */
        if(max == -1){
            max = 0;
        }
        for(Element e : attributes){
            Attribute a = (Attribute) e;
            if(a.getOffset() == -1){
                a.setOffset(++max);
            }
        }
    }
    private void consolidateMethods(List parentMethods) throws SemanticException {
        for(Element e : methods.values()) {
            Method m = (Method) e;
            Method mParent = getParentMethod(m, parentMethods);
            if (parentMethods.contains(m.getName().getLexeme())) {
                if (mParent != null) {
                    checkStatic(m, mParent);
                    if ((!m.getReturnType().getName().getLexeme().equals(mParent.getReturnType().getName().getLexeme()) ||
                            m.getParameters().size() != mParent.getParameters().size())) {
                        throw new SemanticException(SemanticErrorIMessage.methodDoesNotOverrideCorrectly(m));
                    }
                    if (mParent.getModifier() != null && mParent.getModifier().getTokenType().equals(TokenType.reservedAbstract) && m.getModifier() != null &&
                            m.getModifier().getTokenType().equals(TokenType.reservedAbstract) && !isAbstract()) {
                        throw new SemanticException(SemanticErrorIMessage.missingImplementationOnAbstracMethod(m));
                    }
                    if (mParent.getModifier() != null && mParent.getModifier().getTokenType().equals(TokenType.reservedAbstract) && m.getModifier() != null &&
                            m.getModifier().getTokenType().equals(TokenType.reservedAbstract)) {
                        throw new SemanticException(SemanticErrorIMessage.abstractMethodRedefinedAsAbstract(m));
                    }
                    if (mParent.getModifier() != null && mParent.getModifier().getTokenType().equals(TokenType.reservedFinal)) {
                        throw new SemanticException(SemanticErrorIMessage.cannotOverrideFinalMethod(m));
                    }
                    if (m.getModifier() != null && !m.getModifier().getTokenType().equals(TokenType.reservedStatic) && mParent.getModifier() != null && mParent.getModifier().getTokenType().equals(TokenType.reservedStatic)) {
                        throw new SemanticException(SemanticErrorIMessage.methodDoesNotOverrideCorrectly(m));
                    }
                    if (!mParent.getParameters().equals(m.getParameters())) {
                        throw new SemanticException(SemanticErrorIMessage.methodDoesNotOverrideCorrectly(m));
                    }
                    m.setOffset(mParent.getOffset());
                }
            }
        }
    }

    private void checkStatic(Method m, Method mParent) throws SemanticException {
        if(m.getModifier() != null &&
           m.getModifier().getTokenType().equals(TokenType.reservedStatic) &&
                (mParent.getModifier() == null || !mParent.getModifier().getTokenType().equals(TokenType.reservedStatic)))
        {
            throw new SemanticException(SemanticErrorIMessage.methodDoesNotOverrideCorrectly(m));
        }
        if(mParent.getModifier() != null &&
           mParent.getModifier().getTokenType().equals(TokenType.reservedStatic) &&
                (m.getModifier() == null || !m.getModifier().getTokenType().equals(TokenType.reservedStatic)))
        {
            throw new SemanticException(SemanticErrorIMessage.methodDoesNotOverrideCorrectly(m));
        }
    }

    private Method getParentMethod(Method m, List parentMethods) throws SemanticException {
        Token tkParent = parentMethods.getTokenByName(m.getName().getLexeme());
        return (Method) parentMethods.get(tkParent);
    }

    private void consolidateBuilder(Class cParent) throws SemanticException{
        //List parentBuilder = cParent.getBuilderTable();
        Builder myBuilder = null;
        if(!builderTable.isEmpty()) {
            myBuilder = (Builder) builderTable.getFirst();
            myBuilder.setBloque(new NodoBloqueVacio());
        }
        if(builderTable.getFirst() != null){
            if(!builderTable.getFirst().getName().getLexeme().equals(name.getLexeme())){
                throw new SemanticException(SemanticErrorIMessage.builderDoesNotOverrideCorrectly((Builder) builderTable.getFirst()));
            }
        }
        /*
        for(Element b : parentBuilder){
            if(!((Builder)b).getParameters().isEmpty() && myBuilder != null && myBuilder.getParameters().isEmpty()){
                throw new SemanticException(SemanticErrorIMessage.builderDoesNotOverrideCorrectly(myBuilder));
            }

        }
         */
    }
    private void consolidateMethodsFromExtension(List parentMethods) throws SemanticException{
        int max = -1;
        for(Element e : parentMethods.values()){
            Method m = (Method) e;
            if(m.getOffset() > max){
                max = m.getOffset();
            }
            if(!methods.contains(m.getName().getLexeme()) && m.getModifier() != null && m.getModifier().getTokenType().equals(TokenType.reservedAbstract) && !isAbstract()){
                throw new SemanticException(SemanticErrorIMessage.cannotDeclareAbstractMethod(name));
            }
            if(!methods.contains(m.getName().getLexeme())){
                //Method mCopy = new Method(m.getName(), m.getVisibility(), m.getModifier(), m.getReturnType(), m.getEmptyBody());
                //for(Element p : m.getParameters())
                //    mCopy.addParameter((Parameter) p);
                //methods.put(mCopy.getName(), mCopy);
                //mCopy.setBloque(m.getBloque());
                methods.put(m.getName(), m);
            }
        }
        if(max == -1){
            max = 0;
        }
        for(Element e : methods.values()){
            Method m = (Method) e;
            if(m.getOffset() == -1){
                m.setOffset(++max);
            }
        }

    }
    private void consolidateMethodsFromImplementation(List parentMethods) throws SemanticException {
        for(Method m : (Method[]) parentMethods.values()){
            if(!methods.contains(m.getName().getLexeme())){
                throw new SemanticException(SemanticErrorIMessage.classesMustImplementAllInterfaceMethods(name));
            }
        }
    }

    public void check() throws SemanticException {
        super.check();
        for (Element c : builderTable)
        {
            symbolTable().setCurrentService((Builder) c);
            ((Builder) c).check();
        }
    }

    public List getNonStaticMethods() {
        List toReturn = new List();
        for(int i = 0; i < methods.size(); i++){
            if(methods.get(i).getModifier() == null || !methods.get(i).getModifier().getTokenType().equals(TokenType.reservedStatic)){
                toReturn.addLast(methods.get(i));
            }
        }
        return toReturn;
    }

    protected List myMethods() {
        List toReturn = new List();
        for(int i = 0; i < methods.size(); i++){
            if(((Method) methods.get(i)).getCreator() == this){
                toReturn.addLast(methods.get(i));
            }
        }
        return toReturn;
    }

    public void gen(OutputManager o) throws GenerationException {
        sortByOffset(attributes);
        sortByOffset(methods);

        List dynamicMethods = getNonStaticMethods();
        List myMethods = myMethods();

        //setStaticOffsets(methods);
        //setOffsets(dynamicMethods);

        //sortByOffset(dynamicMethods);

        o.gen(CodeGenConfig.DATA);

        if(dynamicMethods.isEmpty()){
            o.gen("VT@" + name.getLexeme() + ": " + Instructions.NOP);
        }
        else{
            Method method = (Method) dynamicMethods.getFirst();
            String firstMethod = method.getName().getLexeme();
            o.gen("VT@" + name.getLexeme() + ": " + Instructions.DW + " lbl_" + firstMethod + "@" + method.getCreator().getName().getLexeme());
            for(int i = 1; i < dynamicMethods.size(); i++){
                method = (Method) dynamicMethods.get(i);
                String methodName = method.getName().getLexeme();
                o.gen(Instructions.DW + " lbl_" + methodName + "@" + method.getCreator().getName().getLexeme());
            }
        }

        o.gen(CodeGenConfig.CODE);
        for(Element e : myMethods.values()){
            Method m = (Method) e;
            m.gen(o, name.getLexeme());
        }
        for(Element e : builderTable){
            Builder c = (Builder) e;
            c.gen(o, name.getLexeme());
        }

        o.gen("");
        o.gen("");
    }


    private void setStaticOffsets(List methods){
        int offset = -1;
        for(OffsetElement o : methods){
            o.setOffset(offset);
        }
    }
}
