package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Interface extends MainElement {
    public Interface(Token m, Token n, AbstractType t, Token i) {
        super(m, n, t, i);
    }
    public Interface(Token m, Token n, AbstractType t, Token i, char it) {

        super(m, n, t, i);
        inheritanceType = it;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        if(inheritance != null && !symbolTable().getInterfaces().contains(inheritance.getLexeme())) {
            if(inheritance != null && symbolTable().getClasses().contains(inheritance.getLexeme()))
                throw new SemanticException(SemanticErrorIMessage.interfaceExtendingAClass(inheritance));
            else
                throw new SemanticException(SemanticErrorIMessage.parentDoesNotExist(inheritance));
        }
        else {
            if (symbolTable().getClassHierarchy().isAncestor(name.getLexeme(), inheritance.getLexeme())){
                throw new SemanticException(SemanticErrorIMessage.circularHierarchy(inheritance));
            }
        }
        for (Element a : attributes.values())
            a.correctDeclaration();
        for (Method m : (Method[]) methods.values())
            m.correctDeclaration();
    }
}
