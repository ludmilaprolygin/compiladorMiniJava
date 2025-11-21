package model.AST.Sentencias.Bloques;

import model.codeGeneration.Instructions;
import outputManager.OutputManager;

public class NodoBloquePrintln extends NodoBloque {
    public NodoBloquePrintln() {
        super(null);
    }
    public void gen(OutputManager o){
        o.gen(Instructions.PRNLN.toString());
    }
}
