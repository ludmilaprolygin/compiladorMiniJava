package model.symbolTable;

import outputManager.OutputManager;

public interface Var {
    public void gen(OutputManager o);
    public void setOffset(int i);
}
