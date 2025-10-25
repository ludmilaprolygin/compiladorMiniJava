package model.AST;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.ClassType;
import model.symbolTable.UniversalType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoVar extends NodoOperando {
    protected AbstractType tipo;
    protected Encadenado encadenado;
    protected boolean isDeclared;
    public NodoVar(Token token) {
        super(token);
        tipo = new UniversalType();
        //exists(token);
    }
    public NodoVar(Token token, AbstractType tipo) {
        super(token);
        this.tipo = tipo;
        //exists(token);
    }

    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    public void setTipo (AbstractType a){
        tipo = a;
    }
    public void declare() { isDeclared = true;}

    @Override
    public AbstractType check() throws SemanticException {
        AbstractType toReturn = isDeclared();
        if(encadenado != null)
            if(tipo instanceof ClassType || tipo instanceof UniversalType)
                toReturn = encadenado.check(tipo);
                //encadenado.check(tipo);
            else
                throw new SemanticException(SemanticErrorIIMessage.primitiveTypesCantReceiveCalls(tipo));
        return toReturn;
    }

    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + token.getLexeme() + " (" + tipo.getClass() + ")\n";
    }

    public AbstractType isDeclared() throws SemanticException {
        AbstractType aType;
        if(!isDeclared)
            aType = super.isDeclared();
        else
            aType = tipo;
        return aType;
    }
}
