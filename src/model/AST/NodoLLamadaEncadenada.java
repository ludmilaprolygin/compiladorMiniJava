package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import utils.exceptions.SemanticException;

import java.util.LinkedList;
import java.util.List;

public class NodoLLamadaEncadenada extends Encadenado {
    protected List<NodoExpresion> parametros;
    public NodoLLamadaEncadenada(Token t){
        nombre = t;
        parametros = new LinkedList<>();
    }
    public NodoLLamadaEncadenada(Token t, Encadenado e){
        nombre = t;
        parametros = new LinkedList<>();
    }
    public NodoLLamadaEncadenada(Token t, Encadenado e, List<NodoExpresion> p){
        nombre = t;
        parametros = p;
    }
    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }
    public void setParametros(List<NodoExpresion> p){
        parametros = p;
    }
    public List<NodoExpresion> getParametros(){
        return parametros;
    }
    @Override
    public void check(AbstractType t) throws SemanticException {

    }
}
