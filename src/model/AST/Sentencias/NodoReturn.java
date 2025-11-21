package model.AST.Sentencias;

import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoExpresionVacia;
import model.AST.Expresiones.NodoThis;
import model.Token;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Comments;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import static model.TokenType.reservedStatic;
import static model.TokenType.reservedVoid;

public class NodoReturn extends NodoSentencia {
    protected NodoExpresion expresion;
    protected Token t;
    protected Service method;

    public NodoReturn(NodoExpresion e, Token token){
        expresion = e;
        t = token;
    }

    @Override
    public void check() throws SemanticException {
        AbstractType aType = expresion.check();

        if(!(expresion instanceof NodoExpresionVacia)) {
            Service s = SymbolTable.symbolTable().getCurrentService();
            if (s instanceof Method m)
                try {
                    m.getReturnType().compatible(aType);
                } catch (Exception e) {
                    throw new SemanticException(SemanticErrorIIMessage.incorrectReturnType(t, "return"));
                }
        }
    }

    public Token getToken() { return t; }
    public void setMethod(Service m) { method = m; }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        toReturn += "return\n";
        toReturn += expresion.toString(depth + 1);
        return toReturn;
    }

    @Override
    public void checkThisOnStaticContext() throws SemanticException {
        if(expresion instanceof NodoThis){
            throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(expresion.getToken()));
        }
    }

    public boolean compatibleWithVoid() {
        return expresion instanceof NodoExpresionVacia;
    }

    public boolean compatibleWithType(AbstractType type) throws SemanticException {
        AbstractType returnType = expresion.check();
        if(type.getName().getLexeme().equals(reservedVoid.getTypeExplanation())){
            return compatibleWithVoid();
        }
        else
            return returnType.compatible(type);
    }

    public AbstractType getType() throws SemanticException {
        return expresion.check();
    }

    public NodoExpresion getExpresion() { return expresion; }

    public NodoReturn hasReturnStatementSomewhere() {
        return this;
    }

    @Override
    public void gen(OutputManager o) {
        int cantParams = method.getParameters().size();
        boolean isStatic = method.getModifier() != null && method.getModifier().getTokenType().equals(reservedStatic);

        if(method instanceof Method m && !m.getReturnType().getName().getLexeme().equals(reservedVoid.getTypeExplanation())){
            expresion.gen(o);
            int retOffset = cantParams + Integer.parseInt(CodeGenConfig.OFFSET_THIS);

            retOffset = !isStatic ? retOffset + 1 : retOffset;
            o.gen(Instructions.STORE + " " + retOffset + Comments.SAVE_RETURN_VALUE.getComment());
        }

        //generarSaltoAlFinalDelMetodo(o);

        int cantVars = method.getBloque().getVariables().size();

        if (cantVars > 0) {
            o.gen(Instructions.FMEM + " " + cantVars + Comments.FREE_VARS.getComment());
        }

        int fMEM = isStatic ? cantParams : cantParams + 1;

        o.epilogue(fMEM);
    }

    private void generarSaltoAlFinalDelMetodo(OutputManager o) {
        String mName = method.getName().getLexeme();

        String cName;
        if (method instanceof Method m) {
            cName = m.getCreator().getName().getLexeme();
        }
        else{
            cName = method.getName().getLexeme();
        }

        String label = "lbl_end_" + mName + "@" + cName;

        o.gen(Instructions.JUMP + " " + label);
    }
}

