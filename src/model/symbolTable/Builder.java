package model.symbolTable;

import model.AST.Sentencias.Bloques.NodoBloqueVacio;
import model.Token;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Builder extends Service {
    public Builder(Token n, Token v) {
        super(n, v);
    }

    @Override
    public void correctDeclaration() throws SemanticException {
        if(!symbolTable().getClasses().contains(name.getLexeme()))
            throw new SemanticException(SemanticErrorIMessage.constructorDoesNotExist(name));
        else {
            Token t = symbolTable().getClasses().getTokenByName(name.getLexeme());
            Class c = symbolTable().getClasses().get(t);
            if(c == null || !c.getBuilderTable().contains(name.getLexeme()))
                throw new SemanticException(SemanticErrorIMessage.constructorDoesNotExist(name));
        }
    }

    @Override
    public void gen(OutputManager o) throws GenerationException {
        o.prologue();


        if(!(bloque instanceof NodoBloqueVacio)){
            bloque.gen(o);
        }

        o.epilogue(parameters.size());
    }

    @Override
    public int getOffset() {
        return -1;
    }

    @Override
    public Token getModifier() {
        return null;
    }

    public AbstractType getType() { return null; }
}
