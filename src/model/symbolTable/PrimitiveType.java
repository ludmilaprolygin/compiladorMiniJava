package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.SyntacticMethod.TipoPrimitivo;

public class PrimitiveType extends AbstractType {
    public PrimitiveType(Token n) {
        super(n);
    }
    @Override
    public void correctDeclaration() throws SemanticException {
        if (firsts.containsToken(TipoPrimitivo, name.getTokenType())) ;
        else
            throw new SemanticException(SemanticErrorMessage.undeclaredType(name));
    }
}