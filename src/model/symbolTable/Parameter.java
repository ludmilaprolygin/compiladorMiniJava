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
        type.correctDeclaration();
    }

    public String toString() {
        return type.toString() + " " + name.getLexeme();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Parameter other = (Parameter) obj;
        return name.getLexeme().equals(other.name.getLexeme()) &&
                type.equals(other.type);
    }
}
