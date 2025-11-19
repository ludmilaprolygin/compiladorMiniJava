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
        o.gen("VT@Object: " + Instructions.NOP);

        o.gen(".CODE");
        o.gen("lbl_debugPrint@Object: ");
        o.gen(Instructions.LOADFP.toString());
        o.gen(Instructions.LOADSP.toString());
        o.gen(Instructions.STOREFP.toString());
        o.gen(Instructions.LOAD + " 3");
        o.gen(Instructions.IPRINT.toString());
        //o.gen(Instructions.PRNLN.toString());
        o.epilogue(1);

        o.gen("");
        o.gen("");
    }
}
