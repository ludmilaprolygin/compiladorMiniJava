package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public class ClassType extends AbstractType {
    protected AbstractType parametricType;
    public ClassType(Token n) {
        super(n);
    }

    public AbstractType getParametricType() {
        return parametricType;
    }
    public void setParametricType(AbstractType parametricType) {
        this.parametricType = parametricType;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        super.correctDeclaration();
        if (name.getTokenType().equals(TokenType.idClase)){
            checkParametricType();
        }
        else
            throw new SemanticException(SemanticErrorMessage.undeclaredType(name));
    }
    protected void checkParametricType() throws SemanticException {
        if (name.getTokenType().equals(TokenType.idClase)) {
            if (parametricType != null) {
                parametricType.correctDeclaration();
            }
        }
        else if (parametricType != null) {
            throw new SemanticException(SemanticErrorMessage.parametricTypeNotAllowed(parametricType.getName()));
        }
    }

    public String toString() {
        String pType = (parametricType != null) ? "<" + parametricType.toString() + ">" : "";
        return name.getLexeme() + pType;
    }

    public boolean equals(AbstractType t) {
        boolean toReturn = super.equals(t);
        if(toReturn && t instanceof ClassType) {
            ClassType type = (ClassType) t;
            toReturn = (parametricType == null && type.parametricType == null) ||
                    (parametricType != null && type.parametricType != null && parametricType.equals(type.parametricType));
        }
        return toReturn;
    }
}
