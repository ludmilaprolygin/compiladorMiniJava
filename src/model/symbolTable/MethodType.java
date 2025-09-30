package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.SyntacticMethod.TipoMetodo;

public class MethodType extends Type {
    public MethodType(Token n) {
        super(n);
    }
    @Override
    public void correctDeclaration() throws SemanticException {
        checkParametricType();
        if (firsts.containsToken(TipoMetodo, name.getTokenType())) ;
        else
            throw new SemanticException(SemanticErrorMessage.undeclaredType(name));
    }
}
