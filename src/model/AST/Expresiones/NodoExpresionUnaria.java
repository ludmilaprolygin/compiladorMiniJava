package model.AST.Expresiones;

import model.Token;
import model.TokenType;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import model.symbolTable.IntType;
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
            try{
                expType.compatible(new IntType(null));
            }
            catch(SemanticException e){
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
            }
        }
        else if (operador.getTokenType() == TokenType.notOp)
            try{
                expType.compatible(new BooleanType(null));
            }
            catch(SemanticException e){
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
            }
        else
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
        return expType;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += operador.getLexeme() + "\n";
        toReturn += ladoDerecho.toString(depth + 1);
        return toReturn;
    }

    @Override
    public Token getToken() {
        return operador;
    }
}
