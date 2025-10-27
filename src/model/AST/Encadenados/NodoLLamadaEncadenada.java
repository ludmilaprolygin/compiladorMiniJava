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
        super(t);
        parametros = new LinkedList<>();
    }
    public NodoLLamadaEncadenada(Token t, Encadenado e){
        super(t, e);
        parametros = new LinkedList<>();
    }
    public NodoLLamadaEncadenada(Token t, Encadenado e, List<NodoExpresion> p){
        super(t, e);
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
    public AbstractType check(AbstractType t) throws SemanticException {
        Token token = SymbolTable.symbolTable().getClasses().getTokenByName(t.getName().getLexeme());
        model.symbolTable.Class c = SymbolTable.symbolTable().getClasses().get(token);
        if(c != null) {
            Table<Method> methods = c.getMethods();
            for(Method method : methods.values()){
                AbstractType methodType = method.getReturnType();
                if(method.getName().getLexeme().equals(nombre.getLexeme())){
                    if(encadenado == null) {
                        encadenado = new EncadenadoVacio();
                        return methodType;
                    }

                    return encadenado.check(methodType);
                }
            }
        }

        throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(nombre));
    }
}
