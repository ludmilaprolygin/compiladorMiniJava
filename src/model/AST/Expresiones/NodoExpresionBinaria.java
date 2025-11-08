package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.Token;
import model.TokenType;
import model.codeGeneration.Instructions;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import model.symbolTable.IntType;
import outputManager.OutputManager;
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
                expTypeRight.compatible(new IntType(operador));
                expTypeLeft.compatible(new IntType(operador));
            }
            catch(Exception e){
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
            }
        }
        else if (operador.getTokenType() == TokenType.orOp ||
                    operador.getTokenType() == TokenType.andOp){
            try {
                expTypeRight.compatible(new BooleanType(operador));
                expTypeLeft.compatible(new BooleanType(operador));
                expTypeRight = new BooleanType(operador);
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
                expTypeRight.compatible(new IntType(operador));
                expTypeLeft.compatible(new IntType(operador));
                expTypeRight = new BooleanType(operador);
            }
            catch(Exception e){
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
            }
        }
        else if (operador.getTokenType() == TokenType.equalsOp ||
                operador.getTokenType() == TokenType.notEqualOp){
            try {
                expTypeRight.compatible(ladoIzquierdo.check());
                expTypeRight = new BooleanType(operador);
            }
            catch(Exception e){
                try {
                    expTypeLeft.compatible(expTypeRight);
                    expTypeRight = new BooleanType(operador);
                }
                catch(Exception e1){
                    throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
                }
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

    @Override
    public Encadenado getLastEncadenado() {
        return new EncadenadoVacio();
    }

    @Override
    public void gen(OutputManager o) {
        ladoIzquierdo.gen(o);
        ladoDerecho.gen(o);
        if(operador.getTokenType() == TokenType.equalsOp){
            o.gen(Instructions.EQ.toString());
        }
        else if(operador.getTokenType() == TokenType.notEqualOp){
            o.gen(Instructions.NE.toString());
        }
        else if (operador.getTokenType() == TokenType.plusOp){
            o.gen(Instructions.ADD.toString());
        }
        else if (operador.getTokenType() == TokenType.minusOp){
            o.gen(Instructions.SUB.toString());
        }
        else if(operador.getTokenType() == TokenType.multOp){
            o.gen(Instructions.MUL.toString());
        }
        else if(operador.getTokenType() == TokenType.divOp){
            o.gen(Instructions.DIV.toString());
        }
        else if(operador.getTokenType() == TokenType.modOp){
            o.gen(Instructions.MOD.toString());
        }
        // TODO: faltan
    }
}
