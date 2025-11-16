package model.AST.Encadenados;

import model.Token;
import model.symbolTable.AbstractType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class EncadenadoVacio extends Encadenado{
    public EncadenadoVacio() {
        super(null, null);
    }

    @Override
    public void setEncadenado(Encadenado encadenado) {

    }
    public Encadenado getEncadenado() {
        return new EncadenadoVacio();
    }

    @Override
    public AbstractType check(AbstractType t) throws SemanticException {
        return t;
    }
    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        return toReturn + ". {encadenado vacio}\n";
    }

    @Override
    public void gen(OutputManager o, AbstractType tipo) { }
}
