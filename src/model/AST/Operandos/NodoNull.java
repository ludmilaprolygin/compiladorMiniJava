package model.AST.Operandos;

import model.Token;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Comments;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import model.symbolTable.Class;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class NodoNull extends NodoOperando{
    public NodoNull(){
        super(null);
    }

    @Override
    public AbstractType check() throws SemanticException {
        Token tk = new Token(null, "Object", -1);
        return new NullType(tk);
    }
    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + "NULL \n";
    }

    public void gen(OutputManager o) {
        o.gen(Instructions.PUSH + " " + CodeGenConfig.NULL_VALUE + Comments.NULL.getComment());
    }
}
