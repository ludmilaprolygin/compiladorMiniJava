package model.AST.Operandos;

import model.Token;
import model.codeGeneration.CodeGenConfig;
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
        String lbl = "lblString@" + token.getRow();
        o.gen(CodeGenConfig.DATA);
        o.gen(lbl + ": DW " + token.getLexeme() + ",0");
        o.gen(CodeGenConfig.CODE);
        o.gen(Instructions.PUSH + " " + lbl);
    }
}
