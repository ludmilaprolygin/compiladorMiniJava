package outputManager;

import model.codeGeneration.Comments;
import model.codeGeneration.Instructions;

import java.io.FileWriter;
import java.io.IOException;

public class OutputManager {
    final FileWriter file_writer;

    public OutputManager(String file_name) {
        try {
            file_writer = new FileWriter(file_name);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void gen(String line) {
        try {
            file_writer.write(line);
            file_writer.write("\n");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void close() {
        try {
            file_writer.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void genHeap() {
        gen("simple_heap_init: " + Instructions.RET.toString() + " 0");
        gen("");

        gen("simple_malloc: " + Instructions.LOADFP);
        gen(Instructions.LOADSP.toString());
        gen(Instructions.STOREFP.toString());
        gen(Instructions.LOADHL.toString());
        gen(Instructions.DUP.toString());
        gen(Instructions.PUSH.toString() + " 1");
        gen(Instructions.ADD.toString());
        gen(Instructions.STORE.toString() + " 4");
        gen(Instructions.LOAD.toString() + " 3");
        gen(Instructions.ADD.toString());
        gen(Instructions.STOREHL.toString());
        gen(Instructions.STOREFP.toString());
        gen(Instructions.RET.toString() + " 1");

        gen("");
        gen("");
    }

    public void prologue(){
        gen(Comments.PROLOGUE_INIT.getComment());
        gen(Instructions.LOADFP.toString() + Comments.FP_REGISTER.getComment());
        gen(Instructions.LOADSP.toString() + Comments.SP_REGISTER.getComment());
        gen(Instructions.STOREFP.toString() + Comments.STORE_STACK_TOP.getComment());
        gen(Comments.PROLOGUE_END.getComment());
    }

    public void epilogue(int parametersSize){
        gen(Comments.EPILOGUE_INIT.getComment());
        gen(Instructions.STOREFP.toString() + Comments.RESTORE_FP.getComment());
        gen(Instructions.RET + " " + parametersSize);
        gen(Comments.EPILOGUE_END.getComment());
    }

    public void printStackTop() {
        gen(Instructions.DUP.toString());
        gen(Instructions.IPRINT.toString());
        gen(Instructions.PRNLN.toString());
    }
}