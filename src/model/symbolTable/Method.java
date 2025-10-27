package model.symbolTable;

import model.AST.Sentencias.NodoReturn;
import model.AST.Sentencias.NodoSentencia;
import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;
import utils.messages.SemanticErrorIMessage;

public class Method extends Service {
    private AbstractType returnType;
    private Token modifier;
    boolean emptyBody;
    boolean pass;

    public Method (Token n, Token v, Token m, AbstractType t) {
        super(n, v);
        returnType = t;
        modifier = m;
        emptyBody = true;
        pass = false;
    }
    public Method (Token n, Token v, Token m, AbstractType t, boolean b) {
        super(n, v);
        returnType = t;
        modifier = m;
        emptyBody = b;
        pass = false;
    }

    public void pass(){ pass = true; }

    @Override
    public void correctDeclaration() throws SemanticException {
        super.correctDeclaration();
        returnType.correctDeclaration();
        //TODO - preguntar
        //if(!returnType.getName().getTokenType().equals(TokenType.reservedVoid) && emptyBody)
        //    throw new SemanticException(SemanticErrorMessage.missingReturnStatement(name));
        if(!emptyBody && modifier != null && modifier.getTokenType().equals(TokenType.reservedAbstract))
            throw new SemanticException(SemanticErrorIMessage.abstractMethodWithBody(name));
        if(modifier != null && modifier.getTokenType().equals(TokenType.reservedStatic) && returnType.getName().getTokenType().equals(TokenType.reservedVoid) &&
            name.getLexeme().equals("main"))
            SymbolTable.symbolTable().setHasMain();
        checkReturnType();
        checkThisOnStaticContext();
    }

    public String toString() {
        String mod = (modifier != null) ? modifier.getLexeme() + " " : "";
        return mod + returnType.toString() + " " +
                name.getLexeme() + "(" + parameters.toString() + ")" + bloque.toString(0);
    }

    public AbstractType getReturnType() { return returnType; }
    public boolean getEmptyBody() { return emptyBody; }
    public void setCompletedBody() { emptyBody = false; }

    public boolean equalSignature(Method method){
        boolean toReturn = super.equalSignature(method);
        toReturn = toReturn && parameters.equals(method.getParameters());
        return toReturn && !returnType.getName().getLexeme().equals(method.getReturnType().getName().getLexeme());
    }

    public Token getModifier () { return modifier; }

    protected void checkReturnType() throws SemanticException {
        NodoReturn ret = bloque.hasReturnStatementSomewhere();
        if(ret != null && returnType.getName().getTokenType().equals(TokenType.reservedVoid) && !ret.compatibleWithVoid())
            throw new SemanticException(SemanticErrorIIMessage.voidMethodWithReturnStatement(ret.getToken()));
        //if(ret != null && returnType != null && !returnType.compatible(ret.getType()))
        //    throw new SemanticException(SemanticErrorIIMessage.incorrectReturnType(ret.getToken()));
        if(ret == null && !returnType.getName().getTokenType().equals(TokenType.reservedVoid) && !pass){
            throw new SemanticException(SemanticErrorIIMessage.missingReturnStatement(name));
        }
        if(ret != null && ret.compatibleWithVoid() && !returnType.getName().getTokenType().equals(TokenType.reservedVoid)){
            throw new SemanticException(SemanticErrorIIMessage.incorrectReturnType(ret.getToken()));
        }

    }

    protected void checkThisOnStaticContext() throws SemanticException {
        if(modifier != null && modifier.getTokenType().equals(TokenType.reservedStatic)){
            bloque.checkThisOnStaticContext();
        }
    }

    public boolean isStatic() {
        return modifier != null && modifier.getTokenType().equals(TokenType.reservedStatic);
    }
}
