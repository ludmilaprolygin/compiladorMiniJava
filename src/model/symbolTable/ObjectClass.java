package model.symbolTable;

import model.Token;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;

public class ObjectClass extends Class {
    public ObjectClass(Token m, Token n, AbstractType t, MainElement i) {
        super(m, n, t, i);
    }

    public void gen(OutputManager o) throws GenerationException {
        o.gen(".DATA");
        o.gen("VT@Object: " + Instructions.DW + " lbl_debugPrint@Object");

        o.gen("");

        o.gen(".CODE");
        o.gen("lbl_debugPrint@Object: " + Instructions.LOAD + " 3");
        o.gen(Instructions.IPRINT.toString());
        o.gen(Instructions.PRNLN.toString());
    }
}
