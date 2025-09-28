package model.symbolTable;

import model.Token;
import utils.exceptions.SemanticException;

public class Parameter extends Element{
    Type type;
    public Parameter(Token n, Type t) {

        super(n);
        type = t;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        ;
    }

    public String toString() {
        return type.getName().getLexeme() + " " + name.getLexeme();
    }
}
