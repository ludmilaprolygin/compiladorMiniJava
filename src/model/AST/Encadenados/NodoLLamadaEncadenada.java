package model.AST.Encadenados;

import model.AST.Expresiones.NodoExpresion;
import model.Token;
import model.symbolTable.*;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

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
        encadenado = e;
    }
    public NodoLLamadaEncadenada(Token t, Encadenado e, List<NodoExpresion> p){
        nombre = t;
        parametros = p;
        encadenado = e;
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
    public AbstractType check(AbstractType t) throws SemanticException {
        Token token = SymbolTable.symbolTable().getClasses().getTokenByName(t.getName().getLexeme());
        model.symbolTable.Class c = SymbolTable.symbolTable().getClasses().get(token);
        Table<Method> methods = c.getMethods();
        for(Method method : methods.values()){
            if(method.getName().getLexeme().equals(nombre.getLexeme()));
            if(encadenado == null)
                encadenado = new EncadenadoVacio();
            return encadenado.check(method.getReturnType());
        }
        throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(nombre));
    }
}
