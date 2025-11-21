package model.AST.Sentencias.Bloques;

import model.codeGeneration.Instructions;
import outputManager.OutputManager;

public class NodoBloquePrintB extends NodoBloque {
    public NodoBloquePrintB() {
        super(null);
    }
    public void gen(OutputManager o){
        o.gen(Instructions.LOAD + " 3");
        o.gen(Instructions.BPRINT.toString());
    }
}
