package model.AST.Operandos;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.AST.Encadenados.NodoVarEncadenada;
import model.AST.Sentencias.Bloques.NodoBloque;
import model.Token;
import model.TokenType;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Comments;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import static model.TokenType.reservedVoid;
import static model.codeGeneration.CodeGenConfig.PARAM_OFFSET;
import static model.symbolTable.SymbolTable.symbolTable;

public class NodoVar extends NodoOperando implements Var {
    protected AbstractType tipo;
    protected Encadenado encadenado;
    protected boolean isDeclared;
    protected Var varAsociada;
    protected int offset;
    protected NodoBloque bloque;

    public NodoVar(Token token) {
        super(token);
        tipo = new UniversalType();
        //encadenado = new EncadenadoVacio();
        //exists(token);
    }
    public NodoVar(Token token, AbstractType tipo) {
        super(token);
        this.tipo = tipo;
        //encadenado = new EncadenadoVacio();
        //exists(token);
    }

    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }

    public void setTipo (AbstractType a){
        tipo = a;
    }
    public void declare() throws SemanticException {
        isDeclared = true;
    }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }

    @Override
    public Token getTokenName() {
        return this.getToken();
    }

    @Override
    public AbstractType check() throws SemanticException {
        AbstractType toReturn = isDeclared();
        if(encadenado != null)
            if(tipo instanceof ClassType || tipo instanceof UniversalType)
                toReturn = encadenado.check(toReturn);
                //encadenado.check(tipo);
            else
                throw new SemanticException(SemanticErrorIIMessage.primitiveTypesCantReceiveCalls(tipo));

        SymbolTable st = SymbolTable.symbolTable();
        Service s = st.getCurrentService();

        //toReturn = st.getCurrentService().getParameters().contains(this.getToken().getLexeme());

        for(Element n : st.getCurrentService().getParameters()){
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme())){
                //toReturn = true;
                varAsociada = (Var) n;
                tipo = ((Parameter) n).getType();
            }
        }

        for(Element e : st.getCurrentClass().getAttributes().values()){
            Attribute n = (Attribute) e;
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme()) && s.getModifier() != null && s.getModifier().getLexeme().equals(TokenType.reservedStatic.getTypeExplanation())){
                //toReturn = true;
                if(varAsociada == null)
                    varAsociada = n;
                throw new SemanticException(SemanticErrorIIMessage.accessToAttributeInStaticContext(getToken()));
            }

            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme()) && s.getModifier() != null){
                //toReturn = true;
                //throw new SemanticException(SemanticErrorIIMessage.accessToAttributeInStaticContext(getToken()));
                tipo = n.getType();
            }
        }

        for(NodoOperando e : st.getCurrentService().getLocalVariables()){
            NodoVar n = (NodoVar) e;
            if(n.getTokenName() != null && this.getToken() != null && n.getTokenName().getLexeme().equals(this.getToken().getLexeme())){
                //toReturn = true;
                varAsociada = n;

            }
        }

        return toReturn;
    }

    public String toString(int depth){
        String toReturn = "\n";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        String encString = encadenado != null ? encadenado.toString(depth + 1) : "";
        return toReturn + token.getLexeme() + " (" + tipo.getClass() + ")\n" + encString;
    }

    public AbstractType isDeclared() throws SemanticException {
        AbstractType aType;
        if(!isDeclared)
            aType = super.isDeclared();
        else {
            aType = tipo;
        }
        return aType;
    }

    public void checkExistance() throws SemanticException {
        boolean toReturn = false;
        SymbolTable st = SymbolTable.symbolTable();
        toReturn = st.getCurrentService().getParameters().contains(this.getToken().getLexeme());
        Service s = st.getCurrentService();
        Token t = null;
        for(Element n : st.getCurrentService().getParameters()){
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme())){
                varAsociada = (Var) n;
                toReturn = true;
            }
            t = n.getName();
        }
        // toReturn = toReturn || st.getCurrentClass().getAttributes().contains(this.getToken().getLexeme());
        for(Element e : st.getCurrentClass().getAttributes().values()){
            Attribute n = (Attribute) e;
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme()) && s.getModifier() != null && s.getModifier().getLexeme().equals(TokenType.reservedStatic.getTypeExplanation())){
                //toReturn = true;
                varAsociada = n;
                throw new SemanticException(SemanticErrorIIMessage.accessToAttributeInStaticContext(getToken()));
            }
            t = n.getName();
        }
        if (toReturn){
            throw new SemanticException(SemanticErrorIIMessage.variableAlreadyExists(token));
        }
        for(NodoOperando e : st.getCurrentService().getBloque().getVariables()){
            NodoVar n = (NodoVar) e;
            if(n.getTokenName() != null && this.getToken() != null && n.getTokenName().getLexeme().equals(this.getToken().getLexeme()) && s.getModifier() != null && s.getModifier().getLexeme().equals(TokenType.reservedStatic.getTypeExplanation())){
                //toReturn = true;
                varAsociada = n;
                throw new SemanticException(SemanticErrorIIMessage.accessToAttributeInStaticContext(getToken()));
            }
            t = n.getTokenName();
        }
        //varAsociada = this;
    }

    public Encadenado getLastEncadenado (){
        if (encadenado == null)
            return null;
        return encadenado.getLastEncadenado();
    }

    public void setBloque (NodoBloque b) {
        bloque = b;
    }

    public Encadenado getEncadenado() {
        return encadenado;
    }

    @Override
    public AbstractType checkLeftValue() throws SemanticException {

         Encadenado ultimo = this.getLastEncadenado();

        if (ultimo instanceof NodoLLamadaEncadenada) {
            throw new SemanticException(SemanticErrorIIMessage.invalidLeftValue(ultimo.getNombre()));
        }

        return this.check();
    }

    public AbstractType getType() {
        return tipo;
    }

    public void setVar(Var v){ varAsociada = v; }

    @Override
    public void gen(OutputManager o) {
        //System.out.println(varAsociada.getTokenName().getLexeme() + " " + varAsociada.getOffset());

        //System.out.println("ASCII DE a: " + (int)'a');
        boolean ladoIzqFinal = esLadoIzq && (encadenado == null || encadenado instanceof EncadenadoVacio);

        if(tipo instanceof CharType)
            token.setLexeme(String.valueOf((int)token.getLexeme().charAt(0)));

        if (varAsociada instanceof Parameter p) {
            int baseParam = CodeGenConfig.PARAM_OFFSET;
            if(symbolTable().getCurrentService() instanceof Method m && m.getModifier() != null && m.getModifier().getTokenType().equals(TokenType.reservedStatic)){
                baseParam--;
            }

            int computedOffset;
            int pOff = p.getOffset();
            //System.out.println("en instanceof de Param: " + p.getOffset());
            computedOffset = pOff + baseParam;


            //System.out.println("computed offset de " + p.getTokenName().getLexeme() + ": " + computedOffset);

            if (ladoIzqFinal) {
                o.gen(Instructions.STORE + " " + computedOffset + Comments.STORE_PARAM.getComment() + " (" + p.getTokenName().getLexeme() + ")");
            } else {
                o.gen(Instructions.LOAD + " " + computedOffset + Comments.LOAD_PARAM.getComment() + " (" + p.getTokenName().getLexeme() + ")");
            }
        }

        else if(varAsociada instanceof Attribute a) {
            o.gen(Instructions.LOAD + " " + CodeGenConfig.OFFSET_THIS);
            if (ladoIzqFinal) {
                o.gen(Instructions.SWAP.toString());
                o.gen(Instructions.STOREREF + " " + a.getOffset() + Comments.ATTRIBUTE_ASSIGNMENT.getComment() + " (" + a.getTokenName().getLexeme() + ")");
            } else {
                 o.gen(Instructions.LOADREF + " " + a.getOffset() + Comments.ATTRIBUTE_ACCESS.getComment() + " (" + a.getTokenName().getLexeme() + ")");
            }
        }
        else if(varAsociada instanceof NodoVar v) {
            setOffsetsForVarLocal();
            //System.out.println("offset local de " + v.getTokenName().getLexeme() + ": " + v.getOffset());
            if (ladoIzqFinal) {
                o.gen(Instructions.STORE + " " + v.getOffset() + Comments.STORE_LOCAL.getComment() + " (" + v.getTokenName().getLexeme() + ")");
            }
            else {
                o.gen(Instructions.LOAD + " " + v.getOffset() + Comments.LOAD_LOCAL.getComment() + " (" + v.getTokenName().getLexeme() + ")");
            }
        }
        //System.out.println(varAsociada.getTokenName().getLexeme() + " " + varAsociada.getOffset());

        AbstractType a;
        if(tipo == null || tipo instanceof UniversalType){
            a = searchType(this);
            tipo = a;
        }

        if(encadenado != null && !(encadenado instanceof EncadenadoVacio)){
            if(esLadoIzq){
                encadenado.setLeftValue();
            }
            encadenado.gen(o, tipo);
        }
    }

    private Service getNodoVarContext(){
        SymbolTable st = SymbolTable.symbolTable();
        return st.getCurrentService();
    }

    private AbstractType searchType(Var v){
        AbstractType toReturn = new UniversalType();
        Service s = this.getNodoVarContext();

        toReturn = s.searchType(v);

        if(toReturn == null || toReturn instanceof UniversalType){
            toReturn = SymbolTable.symbolTable().getCurrentClass().searchType(v);
        }

        return toReturn;
    }

    private void setOffsetsForVarLocal(){
        for(NodoOperando o : symbolTable().getCurrentService().getBloque().getVariables()){
            if(o instanceof NodoVar var){
                if(var.getToken().getLexeme().equals(this.getToken().getLexeme())){
                    this.setOffset(var.getOffset());
                }
            }
        }
    }
}
