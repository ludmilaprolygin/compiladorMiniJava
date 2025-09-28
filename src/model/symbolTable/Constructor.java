package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Constructor extends Service {
    public Constructor(Token n, Token v) {
        super(n, v);
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        if(!symbolTable().getClasses().contains(name.getLexeme()))
            throw new SemanticException(SemanticErrorMessage.constructorDoesNotExist(name));
        else {
            Token t = symbolTable().getClasses().getTokenByName(name.getLexeme());
            Class c = symbolTable().getClasses().get(t);
            if(c == null || !c.getConstructors().contains(name.getLexeme()))
                throw new SemanticException(SemanticErrorMessage.constructorDoesNotExist(name));
        }
    }
}
