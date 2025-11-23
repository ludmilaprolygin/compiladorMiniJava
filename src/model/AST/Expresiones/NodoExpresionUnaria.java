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

public class NodoExpresionUnaria extends NodoExpresion{

    protected Token operador;
    protected NodoExpresion ladoDerecho;
    protected boolean isStatementExpression = false;

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

    @Override
    public Encadenado getLastEncadenado() {
        return new EncadenadoVacio();
    }

    @Override
    public void gen(OutputManager o) {
        ladoDerecho.gen(o);

        if(operador.getTokenType() == TokenType.incrementOp){
            o.gen(Instructions.PUSH + " 1");
            o.gen(Instructions.ADD.toString());
            if(isStatementExpression)
                o.gen(Instructions.DUP.toString());

            ladoDerecho.setEsLadoIzq();
            ladoDerecho.gen(o);
            ladoDerecho.setEsLadoIzq();
        }
        else if(operador.getTokenType() == TokenType.decrementOp){
            o.gen(Instructions.PUSH + " 1");
            o.gen(Instructions.SUB.toString());
            if(isStatementExpression)
                o.gen(Instructions.DUP.toString());

            ladoDerecho.setEsLadoIzq();
            ladoDerecho.gen(o);
            ladoDerecho.setEsLadoIzq();
        }
        else if(operador.getTokenType() == TokenType.plusOp){
            // ????
        }
        else if(operador.getTokenType() == TokenType.minusOp){
            o.gen(Instructions.NEG.toString());
        }
        else if(operador.getTokenType() == TokenType.notOp){
            o.gen(Instructions.NOT.toString());
        }
    }

    public void setIsStatementExpression() {
        isStatementExpression = true;
    }
}
