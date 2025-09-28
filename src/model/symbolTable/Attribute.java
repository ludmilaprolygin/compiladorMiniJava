package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Attribute extends Element {
    private AbstractType type;
    public Attribute(Token n, AbstractType t) {
        super(n);
        type = t;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        MainElement currentClass = symbolTable().getCurrentClass();
        if(currentClass.getAttributes().contains(this.getName().getLexeme()))
           throw new SemanticException(SemanticErrorMessage.attributeAlreadyExists(this.getName()));
        else
           type.correctDeclaration();
    }
}
