package model.AST.Sentencias.Bloques;

import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Comments;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;

public class NodoBloqueToString extends NodoBloque{
    public NodoBloqueToString() {
        super(null);
    }
    public void gen(OutputManager o){
        o.gen(Instructions.LOAD + " " + CodeGenConfig.OFFSET_THIS);
        o.gen(Instructions.LOADREF + " 1" + Comments.ATTRIBUTE_ACCESS.getComment() + " (className)");
        o.gen(Instructions.SPRINT.toString());
    }
}
