package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIMessage;

public class Method extends Service {
    private AbstractType returnType;
    private Token modifier;
    boolean emptyBody;

    public Method (Token n, Token v, Token m, AbstractType t) {
        super(n, v);
        returnType = t;
        modifier = m;
        emptyBody = true;
    }
    public Method (Token n, Token v, Token m, AbstractType t, boolean b) {
        super(n, v);
        returnType = t;
        modifier = m;
        emptyBody = b;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        super.correctDeclaration();
        returnType.correctDeclaration();
        //TODO - preguntar
        //if(!returnType.getName().getTokenType().equals(TokenType.reservedVoid) && emptyBody)
        //    throw new SemanticException(SemanticErrorMessage.missingReturnStatement(name));
        if(!emptyBody && modifier != null && modifier.getTokenType().equals(TokenType.reservedAbstract))
            throw new SemanticException(SemanticErrorIMessage.abstractMethodWithBody(name));

    }

    public String toString() {
        String mod = (modifier != null) ? modifier.getLexeme() + " " : "";
        return mod + returnType.toString() + " " +
                name.getLexeme() + "(" + parameters.toString() + ")";
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
}
