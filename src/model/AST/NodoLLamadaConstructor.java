package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.ClassType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;

import java.util.LinkedList;
import java.util.List;

public class NodoLLamadaConstructor extends NodoExpresion{
    protected ClassType classType;
    protected List<NodoExpresion> parametros;

    public NodoLLamadaConstructor(ClassType classType){
        this.classType = classType;
        parametros = new LinkedList<>();
    }

    public NodoLLamadaConstructor(ClassType classType, List<NodoExpresion> p){
        this.classType = classType;
        parametros = p;
    }

    @Override
    public AbstractType check() throws SemanticException {
        return classType;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        if (parametros.isEmpty())
            return toReturn + "new " + classType.getName().getLexeme() + "() \n";
        else
            return toReturn + "new " + classType.getName().getLexeme() + "(" + parametros.toString()+ ")\n";
    }

    @Override
    public Token getToken() {
        return classType.getName();
    }
}
