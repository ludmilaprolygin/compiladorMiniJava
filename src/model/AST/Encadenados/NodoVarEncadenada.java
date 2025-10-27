package model.AST.Encadenados;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.Attribute;
import model.symbolTable.SymbolTable;
import model.symbolTable.Table;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoVarEncadenada extends Encadenado {
    public NodoVarEncadenada(Token t){
        super(t);
    }
    public NodoVarEncadenada(Token t, Encadenado e){
        super(t, e);
    }
    public void setEncadenado(Encadenado encadenado){
        this.encadenado = encadenado;
    }

    @Override
    public AbstractType check(AbstractType t) throws SemanticException {
        Token token = SymbolTable.symbolTable().getClasses().getTokenByName(t.getName().getLexeme());
        model.symbolTable.Class c = SymbolTable.symbolTable().getClasses().get(token);
        if(c != null){
            Table<Attribute> attributes = c.getAttributes();
            for(Attribute attribute : attributes.values()){
                if(attribute.getName().getLexeme().equals(nombre.getLexeme()));
                if(encadenado == null)
                    encadenado = new EncadenadoVacio();
                return encadenado.check(attribute.getType());
            }
        }
        throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(nombre));
    }
}
