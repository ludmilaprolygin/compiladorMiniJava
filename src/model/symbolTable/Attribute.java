package model.symbolTable;

import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoExpresionAsignacion;
import model.AST.Expresiones.NodoExpresionVacia;
import model.Token;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class Attribute extends OffsetElement implements Var {
    private AbstractType type;
    protected int offset;
    protected String mnemonic;
    protected NodoExpresion initialValue;
    public Attribute(Token n, AbstractType t) {
        super(n);
        type = t;
        offset = -1;
        initialValue = new NodoExpresionVacia();
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        type.correctDeclaration();
    }

    public String getMnemonic() { return mnemonic; }
    public void setMnemonic(String m) { this.mnemonic = m; }
    public AbstractType getType() { return type; }

    public NodoExpresion getValue() { return initialValue; }
    public void setValue(NodoExpresion v) { this.initialValue = v; }

    public String toString() {
        return "Offset: " + offset + " " + type.toString() + " " + super.toString();
    }

    public void setOffset(int offset) { this.offset = offset;}

    @Override
    public Token getTokenName() {
        return this.getName();
    }

    public int getOffset() { return this.offset; }

    @Override
    public Token getModifier() {
        return null;
    }

    @Override
    public void gen(OutputManager o) {

    }
}
