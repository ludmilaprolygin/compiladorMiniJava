package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.UniversalType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class NodoExpresionVacia extends NodoExpresion {
    @Override
    public AbstractType check() throws SemanticException {
        return new UniversalType();
    }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + "{ nodo expresion vacia }\n";
    }

    @Override
    public Token getToken() {
        return null;
    }

    @Override
    public Encadenado getLastEncadenado() {
        return new EncadenadoVacio();
    }

    @Override
    public void gen(OutputManager o) { }
}
