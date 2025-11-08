package model.AST.Sentencias.Bloques;

import model.codeGeneration.Instructions;
import outputManager.OutputManager;

public class NodoBloquePrintCln extends NodoBloque {
    public NodoBloquePrintCln() {
        super(null);
    }
    public void gen(OutputManager o){
        o.gen(Instructions.LOAD + " 3");
        o.gen(Instructions.CPRINT.toString());
        o.gen(Instructions.PRNLN.toString());
    }
}
