package model.AST;

import model.Token;
import model.TokenType;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import model.symbolTable.IntType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

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
            expTypeRight.compatible(new IntType(null));
            expTypeLeft.compatible(new IntType(null));
        }
        else if (operador.getTokenType() == TokenType.orOp ||
                    operador.getTokenType() == TokenType.andOp){
            expTypeRight.compatible(new BooleanType(null));
            expTypeLeft.compatible(new BooleanType(null));
            expTypeRight = new BooleanType(null);
        }
        else if(operador.getTokenType() == TokenType.lesserOp ||
                operador.getTokenType() == TokenType.lesserEqualOp ||
                operador.getTokenType() == TokenType.greaterOp ||
                operador.getTokenType() == TokenType.greaterEqualOp){
            expTypeRight.compatible(new IntType(null));
            expTypeLeft.compatible(new IntType(null));
            expTypeRight = new BooleanType(null);
        }
        else if (operador.getTokenType() == TokenType.equalsOp ||
                operador.getTokenType() == TokenType.notEqualOp){
            expTypeRight.compatible(ladoIzquierdo.check());
            expTypeRight = new BooleanType(null);
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
}
