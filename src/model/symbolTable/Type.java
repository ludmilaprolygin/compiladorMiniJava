package model.symbolTable;

import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.SyntacticMethod.TipoPrimitivo;
import static model.symbolTable.SymbolTable.symbolTable;

public class Type extends Element {
    @Override
    public void correctDeclaration() throws SemanticException {
        if (firsts.containsToken(TipoPrimitivo, name.getTokenType()) ||
            symbolTable().getClasses().contains(name.getLexeme())) ;
        else
            throw new SemanticException(SemanticErrorMessage.undeclaredType(name));
    }
}
