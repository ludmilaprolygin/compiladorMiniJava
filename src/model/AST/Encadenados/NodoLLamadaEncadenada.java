package model.AST.Encadenados;

import model.AST.Expresiones.NodoExpresion;
import model.Token;
import model.symbolTable.*;
import model.symbolTable.Class;
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
        if(t.isPrimitive() || t instanceof VoidType)
            throw new SemanticException(SemanticErrorIIMessage.primitiveTypesCantReceiveCalls(nombre));
        Token token = SymbolTable.symbolTable().getClasses().getTokenByName(t.getName().getLexeme());
        model.symbolTable.Class c = SymbolTable.symbolTable().getClasses().get(token);
        if(c != null) {
            Table<Method> methods = c.getMethods();
            for(Method method : methods.values()){
                AbstractType methodType = method.getReturnType();
                compareArgs(c);
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

    private void compareArgs(Class belongingClass) throws SemanticException {
        Token tokenM = belongingClass.getMethods().getTokenByName(nombre.getLexeme());
        Method m = belongingClass.getMethods().get(tokenM);
        if(m != null){
            if (m.getParameters().size() != parametros.size())
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(nombre));
            for (int i = 0; i < parametros.size(); i++) {
                AbstractType argType = parametros.get(i).check();
                AbstractType paramType = ((Parameter) m.getParameters().get(i)).getType();
                try {
                    paramType.compatible(argType);
                }
                catch (SemanticException e) {
                    throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(nombre));
                }
            }
        }
        else{
            throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(nombre));
        }
    }
}
