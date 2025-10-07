package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

public class Parameter extends Element{
    AbstractType type;
    public Parameter(Token n, AbstractType t) {

        super(n);
        type = t;
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        SymbolTable st = SymbolTable.symbolTable();
        if(st.getCurrentClass().getParametricType() != null &&
            st.getCurrentClass().getParametricType().getName().getLexeme().equals(type.getName().getLexeme())) {}
        else if(type.getName().getTokenType().equals(TokenType.idClase) &&
                (!st.getClasses().contains(type.getName().getLexeme()) && !SymbolTable.symbolTable().getInterfaces().contains(type.getName().getLexeme())))
            throw new SemanticException(SemanticErrorMessage.undeclaredType(type.getName()));
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
