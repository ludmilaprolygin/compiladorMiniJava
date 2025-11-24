package model.symbolTable;

import model.AST.Expresiones.NodoExpresionAsignacion;
import model.AST.Expresiones.NodoExpresionVacia;
import model.AST.Expresiones.NodoLLamadaConstructor;
import model.AST.Operandos.NodoVar;
import model.AST.Sentencias.Bloques.NodoBloqueVacio;
import model.Token;
import model.TokenType;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIMessage;

import java.util.ArrayList;

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
        //System.out.println("entra a gen de builder " + name.getLexeme());

        o.prologue();

//        if(localVarCount > 0) {
//            o.gen(Instructions.RMEM + " " + localVarCount);
//        }

        genToString(o);

        Token tk = symbolTable().getClasses().getTokenByName(name.getLexeme());
        Class c = symbolTable().getClasses().get(tk);
        List attributes = c.getAttributes();

        for(OffsetElement a : attributes) {
            if(a instanceof Attribute attr) {
                if(!(attr.getValue() instanceof NodoExpresionVacia)){
                    Token t = new Token(TokenType.assignOp, "=", attr.getName().getRow());
                    NodoVar v = new NodoVar(attr.getName(), attr.getType());
                    v.setVar(attr);
                    NodoExpresionAsignacion nodo = new NodoExpresionAsignacion(v, attr.getValue(), t);
                    nodo.gen(o);
                }
            }
        }

        bloque.gen(o);

//        if(localVarCount > 0) {
//            o.gen(Instructions.FMEM + " " + localVarCount);
//        }

        o.epilogue(parameters.size() + 1);
    }

    private void genToString(OutputManager o) throws GenerationException {
        String lbl = "className@" + name.getLexeme();

        o.gen(CodeGenConfig.DATA);
        o.gen(lbl + ": DW \"" + name.getLexeme() + "\",0");

        o.gen(CodeGenConfig.CODE);
        o.gen(Instructions.PUSH + " " + lbl);
        o.gen(Instructions.LOAD + " " + CodeGenConfig.OFFSET_THIS);
        o.gen(Instructions.SWAP.toString());
        o.gen(Instructions.STOREREF + " " + 1);
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
