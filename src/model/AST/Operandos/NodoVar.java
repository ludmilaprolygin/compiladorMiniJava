package model.AST.Operandos;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.AST.Sentencias.Bloques.NodoBloque;
import model.Token;
import model.TokenType;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import static model.TokenType.reservedVoid;
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

        if(varAsociada == null)
            varAsociada = this;

        return toReturn;
    }

    public String toString(int depth){
        String toReturn = "";
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


        if (varAsociada instanceof Parameter p) {
            int baseParamDyn = CodeGenConfig.PARAM_OFFSET_DYNAMIC;
            int computedOffset;
            int pOff = p.getOffset();
            if (pOff <= 0 || pOff >= baseParamDyn) {
                computedOffset = pOff;
            } else {
                computedOffset = pOff + baseParamDyn;
            }

            if (esLadoIzq) {
                o.gen(Instructions.STORE + " " + computedOffset);
            } else {
                o.gen(Instructions.LOAD + " " + computedOffset);
            }
        }

        else if(varAsociada instanceof Attribute a) {
            o.gen(Instructions.LOAD + " " + CodeGenConfig.OFFSET_THIS);
            if (esLadoIzq) {
                o.gen(Instructions.SWAP.toString());
                o.gen(Instructions.STOREREF + " " + a.getOffset());
            } else {
                 o.gen(Instructions.LOADREF + " " + a.getOffset());
            }
        }
        else if(varAsociada instanceof NodoVar v) {
            setOffsetsForVarLocal();
            //System.out.println("offset local de " + v.getTokenName().getLexeme() + ": " + v.getOffset());
            if (esLadoIzq) {
                o.gen(Instructions.STORE + " " + v.getOffset());
            }
            else {
                o.gen(Instructions.LOAD + " " + v.getOffset());
            }
        }
        //System.out.println(varAsociada.getTokenName().getLexeme() + " " + varAsociada.getOffset());

        AbstractType a;
        if(tipo == null || tipo instanceof UniversalType){
            a = searchType(this);
            tipo = a;
        }

        if(encadenado != null && !(encadenado instanceof EncadenadoVacio)){
            encadenado.gen(o, tipo);
        }

        generateReturnType(o);

    }


    public void generateReturnType(OutputManager o) {
        if (tipo == null) return;
        if (!tipo.getName().getLexeme().equals(reservedVoid.getTypeExplanation())) return;
        if (tipo instanceof ClassType) {
            // los objetos se pasan como puntero, nada que hacer
        } else {
            // tipos primitivos: NO hacer LOADREF
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
