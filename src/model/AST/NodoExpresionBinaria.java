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
        AbstractType expType = ladoDerecho.check();
        if(operador.getTokenType() == TokenType.plusOp ||
                operador.getTokenType() == TokenType.minusOp ||
                operador.getTokenType() == TokenType.multOp ||
                operador.getTokenType() == TokenType.divOp ||
                operador.getTokenType() == TokenType.modOp)
        {
            expType.compatible(new IntType(null));
        }
        else if (operador.getTokenType() == TokenType.orOp ||
                    operador.getTokenType() == TokenType.andOp){
            expType.compatible(new BooleanType(null));
            expType = new BooleanType(null);
        }
        else if(operador.getTokenType() == TokenType.lesserOp ||
                operador.getTokenType() == TokenType.lesserEqualOp ||
                operador.getTokenType() == TokenType.greaterOp ||
                operador.getTokenType() == TokenType.greaterEqualOp){
            expType.compatible(new IntType(null));
            expType = new BooleanType(null);
        }
        else if (operador.getTokenType() == TokenType.equalsOp ||
                operador.getTokenType() == TokenType.notEqualOp){
            expType.compatible(ladoIzquierdo.check());
            expType = new BooleanType(null);
        }
        return expType;
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
