package model.AST.Operandos;

import model.Token;
import model.codeGeneration.Instructions;
import model.symbolTable.AbstractType;
import model.symbolTable.ClassType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class NodoStringLiteral extends NodoOperando{

    public NodoStringLiteral(Token token) {
        super(token);
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new ClassType(token);
    }
    public void gen(OutputManager o){
        //o.gen(";-------------------- STRING ------------------------------");
    }
}
