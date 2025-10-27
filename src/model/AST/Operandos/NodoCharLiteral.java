package model.AST.Operandos;

import model.AST.Encadenados.Encadenado;
import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.CharType;
import utils.exceptions.SemanticException;

public class NodoCharLiteral extends NodoOperando {
    public NodoCharLiteral(Token token){
        super(token);
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new CharType(token);
    }
}


