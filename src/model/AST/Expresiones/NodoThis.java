package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.Token;
import model.symbolTable.*;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;


public class NodoThis extends NodoExpresion {
    protected Encadenado encadenado;
    @Override
    public AbstractType check() throws SemanticException {
        Service s = SymbolTable.symbolTable().getCurrentService();
        if(s instanceof Method m)
            if(m.isStatic())
                throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(encadenado.getNombre(), "this"));
        AbstractType toReturn = new ClassType(SymbolTable.symbolTable().getCurrentClass().getName());
        if(encadenado != null)
            return encadenado.check(toReturn);
        return toReturn;
    }

    @Override
    public String toString(int depth) {
        return "";
    }

    @Override
    public Token getToken() {
        return null;
    }

    public void setEncadenado(Encadenado e){
        encadenado = e;
    }

    public Encadenado getLastEncadenado (){
        if (encadenado == null)
            return null;
        return encadenado.getLastEncadenado();
    }

    public Encadenado getEncadenado() {
        return encadenado;
    }

    @Override
    public AbstractType checkLeftValue() throws SemanticException {
        Encadenado ultimo = this.getLastEncadenado();

        if (ultimo == null) {
            throw new SemanticException(SemanticErrorIIMessage.invalidLeftValue(this.getToken()));
        }

        if (ultimo instanceof NodoLLamadaEncadenada) {
            throw new SemanticException(SemanticErrorIIMessage.invalidLeftValue(ultimo.getNombre()));
        }
        return this.check();
    }
}
