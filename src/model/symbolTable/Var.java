package model.symbolTable;

import model.Token;
import outputManager.OutputManager;

public interface Var {
    public void gen(OutputManager o);
    public void setOffset(int i);
    public Token getTokenName();

    int getOffset();

    String getMnemonic();
}
