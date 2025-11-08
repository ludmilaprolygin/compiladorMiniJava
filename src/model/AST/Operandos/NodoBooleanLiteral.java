package model.AST.Operandos;

import model.Token;
import model.TokenType;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Instructions;
import model.symbolTable.AbstractType;
import model.symbolTable.BooleanType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class NodoBooleanLiteral extends NodoOperando {
    public NodoBooleanLiteral(Token token){
        super(token);
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new BooleanType(token);
    }

    @Override
    public void gen(OutputManager o) {
        if(token.getTokenType() == TokenType.boolTrue)
            o.gen(Instructions.PUSH + " " + CodeGenConfig.TRUE_VALUE);
        else
            o.gen(Instructions.PUSH + " " + CodeGenConfig.FALSE_VALUE);
    }
}
