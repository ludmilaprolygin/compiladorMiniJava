package model.AST.Encadenados;

import model.Token;
import model.codeGeneration.Comments;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoVarEncadenada extends Encadenado {
    private Attribute associatedAttribute;
    public NodoVarEncadenada(Token t){
        super(t);
        isLeftValue = false;
    }
    public NodoVarEncadenada(Token t, Encadenado e){
        super(t, e);
    }
    public void setEncadenado(Encadenado encadenado){
        this.encadenado = encadenado;
    }

    @Override
    public AbstractType check(AbstractType t) throws SemanticException {
        if(t.isPrimitive() || t instanceof VoidType)
            throw new SemanticException(SemanticErrorIIMessage.primitiveTypesCantReceiveCalls(nombre));
        Token token = SymbolTable.symbolTable().getClasses().getTokenByName(t.getName().getLexeme());
        model.symbolTable.Class c = SymbolTable.symbolTable().getClasses().get(token);
        if(c != null){
            List attributes = c.getAttributes();
            for(Element e : attributes.values()){
                Attribute attribute = (Attribute) e;
                associatedAttribute = attribute;
                if(attribute.getName().getLexeme().equals(nombre.getLexeme())){
                    if(encadenado == null)
                        encadenado = new EncadenadoVacio();
                    return encadenado.check(attribute.getType());
                }
            }
        }
        throw new SemanticException(SemanticErrorIIMessage.variableDoesNotExist(nombre));
    }

    @Override
    public void gen(OutputManager o, AbstractType tipo) {
        boolean ladoIzqFinal = isLeftValue && (encadenado == null || encadenado instanceof EncadenadoVacio);

        if(!ladoIzqFinal){
            o.gen(Instructions.LOADREF + " " + associatedAttribute.getOffset() + Comments.ATTRIBUTE_ACCESS.getComment(associatedAttribute.getName().getLexeme()));
        }
        else{
            o.gen(Instructions.SWAP.toString());
            o.gen(Instructions.STOREREF + " " + associatedAttribute.getOffset() + Comments.ATTRIBUTE_ASSIGNMENT.getComment(associatedAttribute.getName().getLexeme()));
        }


        if (encadenado != null && !(encadenado instanceof EncadenadoVacio)) {
            if(isLeftValue){
                encadenado.setLeftValue();
            }
            encadenado.gen(o, associatedAttribute.getType());
        }
    }
}
