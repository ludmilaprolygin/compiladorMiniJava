package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Operandos.NodoVar;
import model.AST.Sentencias.NodoIf;
import model.AST.Sentencias.NodoSentenciaConExpresion;
import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import model.symbolTable.UniversalType;
import outputManager.OutputManager;
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

        AbstractType condType = condicion.check();
        if (! (new BooleanType(null)).compatible(condType) ) {
            throw new SemanticException(SemanticErrorIIMessage.optionsMustBeCompatibleWithOperandType(condicion.getToken()));
        }

        AbstractType tipoTrue = sTrue.check();
        AbstractType tipoFalse = sFalse.check();

        if (tipoTrue.compatible(tipoFalse)) {
            return tipoTrue;
        }
        else if (tipoFalse.compatible(tipoTrue)) {
            return tipoFalse;
        }
        else {
            throw new SemanticException(SemanticErrorIIMessage.optionsMustBeCompatibleWithOperandType(sTrue.getToken()));
        }
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

    @Override
    public Encadenado getLastEncadenado() {
        return new EncadenadoVacio();
    }

    @Override
    public void gen(OutputManager o) {
        NodoSentenciaConExpresion st = new NodoSentenciaConExpresion(sTrue);
        NodoSentenciaConExpresion sf = new NodoSentenciaConExpresion(sFalse);
        NodoIf nodoIf = new NodoIf(condicion, st, sf);
        nodoIf.gen(o);
    }
}
