package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.SyntacticMethod.Tipo;

public class Type extends PrimitiveType {
    protected Type parametricType;
    public Type(Token n) {
        super(n);
    }
    public Type(Token n, Type p) {
        super(n);
        parametricType = p;
    }

    public Type getParametricType() {
        return parametricType;
    }

    public void setParametricType(Type parametricType) {
        this.parametricType = parametricType;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        if (firsts.containsToken(Tipo, name.getTokenType())){
            if (name.getTokenType().equals(TokenType.idClase)) {
                if (parametricType != null) {
                    parametricType.correctDeclaration();
                }
            }
            else if (parametricType != null) {
                throw new SemanticException(SemanticErrorMessage.parametricTypeNotAllowed(name));
            }
        }
        else
            throw new SemanticException(SemanticErrorMessage.undeclaredType(name));
    }
}
