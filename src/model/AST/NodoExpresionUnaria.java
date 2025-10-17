package model.AST;

import model.Token;
import model.TokenType;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import model.symbolTable.IntType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoExpresionUnaria extends NodoExpresion{

    protected Token operador;
    protected NodoExpresion ladoDerecho;

    public NodoExpresionUnaria(Token t, NodoExpresion d){
        operador = t;
        ladoDerecho = d;
    }

    @Override
    public AbstractType check() throws SemanticException {
        AbstractType expType = ladoDerecho.check();
        if(operador.getTokenType() == TokenType.plusOp ||
           operador.getTokenType() == TokenType.minusOp ||
           operador.getTokenType() == TokenType.incrementOp ||
           operador.getTokenType() == TokenType.decrementOp)
        {
           expType.compatible(new IntType(null));
        }
        else if (operador.getTokenType() == TokenType.notOp)
            expType.compatible(new BooleanType(null));
        else
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
        return expType;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += operador.getLexeme() + "\n";
        for (int i = 0; i < depth * 3; i++)
            toReturn += " ";
        toReturn += ladoDerecho.toString(depth + 1);
        return toReturn;
    }
}
