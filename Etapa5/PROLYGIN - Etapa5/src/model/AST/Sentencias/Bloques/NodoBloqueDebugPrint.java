package model.AST.Sentencias.Bloques;

import model.codeGeneration.Instructions;
import outputManager.OutputManager;

public class NodoBloqueDebugPrint extends NodoBloque{
    public NodoBloqueDebugPrint() {
        super(null);
    }
    public void gen(OutputManager o){
        o.gen(Instructions.LOAD + " 3");
        o.gen(Instructions.IPRINT.toString());
    }
}
