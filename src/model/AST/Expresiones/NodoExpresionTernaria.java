package model.AST.Expresiones;

import model.AST.Operandos.NodoVar;
import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoExpresionTernaria extends NodoExpresion{

    protected Token operador;
    protected NodoExpresion condicion;
    protected NodoExpresion sTrue;
    protected NodoExpresion sFalse;

    public NodoExpresionTernaria(Token t, NodoExpresion j, NodoExpresion i, NodoExpresion d){
        operador = t;
        condicion = j;
        sTrue = i;
        sFalse = d;
    }

    @Override
    public AbstractType check() throws SemanticException {
        AbstractType condicionType = condicion.check();
        AbstractType sTrueType = sTrue.check();
        AbstractType sFalseType = sFalse.check();
        try{
            condicionType.compatible(new BooleanType(null));
        }
        catch(SemanticException e){
            throw new SemanticException(SemanticErrorIIMessage.firstOperandMustBeBoolean(condicion.getToken()));
        }
        try{
            sTrueType.compatible(sFalseType);
            sTrueType.compatible(new NodoVar(operador).check());
        }
        catch(SemanticException e){
            throw new SemanticException(SemanticErrorIIMessage.optionsMustBeCompatibleWithOperandType(sTrueType.getName()));
        }
        return sTrueType;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += operador.getLexeme() + "\n";
        toReturn += condicion.toString(depth + 1);
        toReturn += sTrue.toString(depth + 1);
        toReturn += sFalse.toString(depth + 1);
        return toReturn;
    }

    @Override
    public Token getToken() {
        return operador;
    }
}
