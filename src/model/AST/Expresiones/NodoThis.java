package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.AST.Encadenados.NodoVarEncadenada;
import model.Token;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;


public class NodoThis extends NodoExpresion {
    protected Encadenado encadenado;
    protected model.symbolTable.Class referenceClass;
    protected AbstractType classType;
    @Override
    public AbstractType check() throws SemanticException {
        Service s = SymbolTable.symbolTable().getCurrentService();
        if(s instanceof Method m)
            if(m.isStatic())
                throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(encadenado.getNombre(), "this"));
        AbstractType toReturn = new ClassType(SymbolTable.symbolTable().getCurrentClass().getName());
        if(encadenado != null)
            return encadenado.check(toReturn);
        classType = toReturn;
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
    public model.symbolTable.Class getReferenceClass() {
        return referenceClass;
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

    @Override
    public void gen(OutputManager o) {
        o.gen(Instructions.LOAD + " " + CodeGenConfig.OFFSET_THIS);
        if(encadenado != null && !(encadenado instanceof EncadenadoVacio)){
            if(encadenado instanceof NodoVarEncadenada v){
                v.setLeftValue();
                encadenado.gen(o, classType);
                v.setLeftValue();
            } else {
                encadenado.gen(o, classType);
            }

        }
    }
}
