package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoExpresionAsignacion extends NodoExpresion{
    protected NodoExpresion ladoIzquierdo;
    protected NodoExpresion ladoDerecho;
    protected Token operador;

    public NodoExpresionAsignacion(NodoExpresion l, NodoExpresion r, Token o){
        ladoIzquierdo = l;
        ladoDerecho = r;
        operador = o;
    }
    @Override
    public AbstractType check() throws SemanticException {
        AbstractType left = ladoIzquierdo.check();
        AbstractType right = ladoDerecho.check();
        if(ladoIzquierdo instanceof NodoExpresionBinaria)
            throw new SemanticException(SemanticErrorIIMessage.composabilityNotAllowed(((NodoExpresionBinaria) ladoIzquierdo).getOperador()));
        if(!(ladoIzquierdo instanceof NodoOperando) && !(ladoIzquierdo instanceof NodoExpresionUnaria))
            throw new SemanticException(SemanticErrorIIMessage.composabilityNotAllowed(operador));
        try{
            left.compatible(right);
        } catch (SemanticException e){
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(operador));
        }
        return right;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "=\n";
        toReturn += ladoIzquierdo.toString(depth + 1);
        toReturn += ladoDerecho.toString(depth + 1);
        return toReturn;
    }

    public Token getToken() {
        return operador;
    }
}
