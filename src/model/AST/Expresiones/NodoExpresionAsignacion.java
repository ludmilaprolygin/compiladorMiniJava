package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.AST.Operandos.NodoOperando;
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
        if(ladoIzquierdo.getLastEncadenado() instanceof NodoLLamadaEncadenada)
            throw new SemanticException(SemanticErrorIIMessage.composabilityNotAllowed(operador));

        ladoIzquierdo.checkLeftValue();

        AbstractType left = ladoIzquierdo.check();
        AbstractType right = ladoDerecho.check();
        if(ladoIzquierdo instanceof NodoExpresionBinaria)
            throw new SemanticException(SemanticErrorIIMessage.composabilityNotAllowed(((NodoExpresionBinaria) ladoIzquierdo).getOperador()));
        if(ladoIzquierdo instanceof NodoExpresionTernaria)
            throw new SemanticException(SemanticErrorIIMessage.composabilityNotAllowed(operador));
        if(ladoIzquierdo instanceof NodoLLamadaConstructor)
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

    @Override
    public Encadenado getLastEncadenado() {
        return new EncadenadoVacio();
    }
}
