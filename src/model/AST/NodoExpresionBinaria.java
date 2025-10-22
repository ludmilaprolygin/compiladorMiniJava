package model.AST;

import model.Token;
import model.TokenType;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import model.symbolTable.IntType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoExpresionBinaria extends NodoExpresion {
    protected Token operador;
    protected NodoExpresion ladoIzquierdo;
    protected NodoExpresion ladoDerecho;

    public NodoExpresionBinaria(Token t, NodoExpresion i, NodoExpresion d){
        operador = t;
        ladoIzquierdo = i;
        ladoDerecho = d;
    }

    public Token getOperador() { return operador; }

    @Override
    public AbstractType check() throws SemanticException {
        AbstractType expTypeRight = ladoDerecho.check();
        AbstractType expTypeLeft = ladoIzquierdo.check();
        if(operador.getTokenType() == TokenType.plusOp ||
                operador.getTokenType() == TokenType.minusOp ||
                operador.getTokenType() == TokenType.multOp ||
                operador.getTokenType() == TokenType.divOp ||
                operador.getTokenType() == TokenType.modOp)
        {
            try{
                expTypeRight.compatible(new IntType(null));
                expTypeLeft.compatible(new IntType(null));
            }
            catch(Exception e){
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
            }
        }
        else if (operador.getTokenType() == TokenType.orOp ||
                    operador.getTokenType() == TokenType.andOp){
            try {
                expTypeRight.compatible(new BooleanType(null));
                expTypeLeft.compatible(new BooleanType(null));
                expTypeRight = new BooleanType(null);
            }
            catch(Exception e){
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
            }
        }
        else if(operador.getTokenType() == TokenType.lesserOp ||
                operador.getTokenType() == TokenType.lesserEqualOp ||
                operador.getTokenType() == TokenType.greaterOp ||
                operador.getTokenType() == TokenType.greaterEqualOp){
            try {
                expTypeRight.compatible(new IntType(null));
                expTypeLeft.compatible(new IntType(null));
                expTypeRight = new BooleanType(null);
            }
            catch(Exception e){
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
            }
        }
        else if (operador.getTokenType() == TokenType.equalsOp ||
                operador.getTokenType() == TokenType.notEqualOp){
            try {
                expTypeRight.compatible(ladoIzquierdo.check());
                expTypeRight = new BooleanType(null);
            }
            catch(Exception e){
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
            }
        }
        return expTypeRight;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += operador.getLexeme() + "\n";
        toReturn += ladoIzquierdo.toString(depth + 1);
        toReturn += ladoDerecho.toString(depth + 1);
        return toReturn;
    }

    @Override
    public Token getToken() {
        return operador;
    }
}
