package model.AST.Operandos;

import model.Token;
import model.codeGeneration.Instructions;
import model.symbolTable.AbstractType;
import model.symbolTable.IntType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class NodoIntLiteral extends NodoOperando {
    public NodoIntLiteral(Token token){
        super(token);
    }

    public AbstractType check() throws SemanticException {
        return new IntType(token);
    }

    @Override
    public void gen(OutputManager o) {
        o.gen(Instructions.PUSH + " " + token.getLexeme());
    }
}
