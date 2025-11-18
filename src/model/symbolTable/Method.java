package model.symbolTable;

import model.AST.Sentencias.Bloques.NodoBloqueVacio;
import model.AST.Sentencias.NodoReturn;
import model.Token;
import model.TokenType;
import model.codeGeneration.Instructions;
import outputManager.OutputManager;
import utils.exceptions.GenerationException;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;
import utils.messages.SemanticErrorIMessage;

public class Method extends Service {
    private AbstractType returnType;
    private Token modifier;
    boolean emptyBody;
    boolean pass;
    protected int offset;
    protected MainElement creator;

    public Method (Token n, Token v, Token m, AbstractType t, MainElement c) {
        super(n, v);
        returnType = t;
        modifier = m;
        emptyBody = true;
        pass = false;
        offset = -1;
        creator = c;
    }
    public Method (Token n, Token v, Token m, AbstractType t, boolean b, MainElement c) {
        super(n, v);
        returnType = t;
        modifier = m;
        emptyBody = b;
        pass = false;
        offset = -1;
        creator = c;
    }

    public void pass(){ pass = true; }
    public MainElement getCreator(){ return creator; }

    @Override
    public void correctDeclaration() throws SemanticException {
        super.correctDeclaration();
        returnType.correctDeclaration();
        //TODO - preguntar
        //if(!returnType.getName().getTokenType().equals(TokenType.reservedVoid) && emptyBody)
        //    throw new SemanticException(SemanticErrorMessage.missingReturnStatement(name));
        if(!emptyBody && modifier != null && modifier.getTokenType().equals(TokenType.reservedAbstract))
            throw new SemanticException(SemanticErrorIMessage.abstractMethodWithBody(name));
        if(modifier != null && modifier.getTokenType().equals(TokenType.reservedStatic) && returnType.getName().getTokenType().equals(TokenType.reservedVoid) &&
            name.getLexeme().equals("main"))
            SymbolTable.symbolTable().setHasMain(creator);
        checkReturnType();
        checkThisOnStaticContext();
    }

    @Override
    public void gen(OutputManager o) throws GenerationException {
        if(bloque instanceof NodoBloqueVacio){
            o.gen(Instructions.NOP.toString());
        } else {
            o.prologue();

            int cantVars = localVarCount;
            boolean isVoid = this.returnType.getName().getTokenType()
                    .equals(TokenType.reservedVoid);
            int returnSlot = isVoid ? 0 : 1;
            
            o.gen(Instructions.RMEM + " " + (returnSlot + cantVars));

            bloque.gen(o);

            if(!isVoid){
                int retOffset = cantVars;
                o.gen(Instructions.STORE + " " + retOffset);
            }

            String methodName = getName().getLexeme();
            String className  = getCreator().getName().getLexeme();
            String lblEnd = "lbl_end_" + methodName + "@" + className;

            o.gen(lblEnd + ": " + Instructions.NOP);

            o.gen(Instructions.FMEM + " " + (returnSlot + cantVars));

            o.epilogue(parameters.size());
        }
    }


    public String toString() {
        String mod = (modifier != null) ? modifier.getLexeme() + " " : "";
        return "Offset: " + offset + " " + mod + returnType.toString() + " " +
                name.getLexeme() + "(" + parameters.toString() + ")\n"; //+ bloque.toString(0);
    }

    public AbstractType getReturnType() { return returnType; }
    public AbstractType getType() { return returnType; }
    public boolean getEmptyBody() { return emptyBody; }
    public void setCompletedBody() { emptyBody = false; }

    public boolean equalSignature(Method method){
        boolean toReturn = super.equalSignature(method);
        toReturn = toReturn && parameters.equals(method.getParameters());
        return toReturn && !returnType.getName().getLexeme().equals(method.getReturnType().getName().getLexeme());
    }

    public Token getModifier () { return modifier; }

    protected void checkReturnType() throws SemanticException {
        NodoReturn ret = bloque.hasReturnStatementSomewhere();
        if(ret != null && returnType.getName().getTokenType().equals(TokenType.reservedVoid) && !ret.compatibleWithVoid())
            throw new SemanticException(SemanticErrorIIMessage.voidMethodWithReturnStatement(ret.getToken()));
        //if(ret != null && returnType != null && !returnType.compatible(ret.getType()))
        //    throw new SemanticException(SemanticErrorIIMessage.incorrectReturnType(ret.getToken()));
        if(ret == null && !returnType.getName().getTokenType().equals(TokenType.reservedVoid) && !pass){
            throw new SemanticException(SemanticErrorIIMessage.missingReturnStatement(name));
        }
        if(ret != null && ret.compatibleWithVoid() && !returnType.getName().getTokenType().equals(TokenType.reservedVoid)){
            throw new SemanticException(SemanticErrorIIMessage.incorrectReturnType(ret.getToken()));
        }
        //checkReturnClause();
    }

    protected void checkThisOnStaticContext() throws SemanticException {
        if(modifier != null && modifier.getTokenType().equals(TokenType.reservedStatic)){
            try{
                bloque.checkThisOnStaticContext();
            }
            catch(Exception e){
                throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(modifier));
            }
        }
    }

    public boolean isStatic() {
        return modifier != null && modifier.getTokenType().equals(TokenType.reservedStatic);
    }

    public void setOffset(int offset) { this.offset = offset;}
    public int getOffset() { return this.offset; }
}
