package model.AST.Sentencias.Bloques;

import model.codeGeneration.Instructions;
import outputManager.OutputManager;

public class NodoBloquePrintS extends NodoBloque {
    public NodoBloquePrintS() {
        super(null);
    }
    public void gen(OutputManager o){
        o.gen(Instructions.LOAD + " 3");
        o.gen(Instructions.SPRINT.toString());
    }
}
