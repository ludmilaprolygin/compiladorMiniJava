package model.symbolTable;

import model.Token;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;

public class ObjectClass extends Class {
    public ObjectClass(Token m, Token n, AbstractType t, MainElement i) {
        super(m, n, t, i);
    }
}
