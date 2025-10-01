package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class Method extends Service {
    private AbstractType returnType;
    private Token modifier;

    public Method (Token n, Token v, Token m, AbstractType t) {
        super(n, v);
        returnType = t;
        modifier = m;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        super.correctDeclaration();
        returnType.correctDeclaration();
    }

    public String toString() {
        String mod = (modifier != null) ? modifier.getLexeme() + " " : "";
        return mod + returnType.toString() + " " +
                name.getLexeme() + "(" + parameters.toString() + ")";
    }

    public AbstractType getReturnType() { return returnType; }

    public boolean equalSignature(Method method){
        boolean toReturn = super.equalSignature(method);
        if(toReturn && !returnType.equals(method.getReturnType())) {
            toReturn = false;
        }
        return toReturn;
    }

    public Token getModifier () { return modifier; }
}
