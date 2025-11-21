package model.AST.Sentencias.Bloques;

import model.codeGeneration.Instructions;
import outputManager.OutputManager;

public class NodoBloqueRead extends NodoBloque {
    public NodoBloqueRead() {
        super(null);
    }

    public void gen(OutputManager o){
        o.gen(Instructions.READ.toString());
        o.gen(Instructions.PUSH + " 48");
        o.gen(Instructions.SUB.toString());
        o.gen(Instructions.STORE + " 3");
    }
}
