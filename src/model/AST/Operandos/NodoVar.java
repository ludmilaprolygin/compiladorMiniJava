package model.AST.Operandos;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.Token;
import model.TokenType;
import model.symbolTable.*;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoVar extends NodoOperando implements Var {
    protected AbstractType tipo;
    protected Encadenado encadenado;
    protected boolean isDeclared;
    protected Var var;
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
                tipo = ((Parameter) n).getType();
            }
        }

        for(Element e : st.getCurrentClass().getAttributes().values()){
            Attribute n = (Attribute) e;
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme()) && s.getModifier() != null && s.getModifier().getLexeme().equals(TokenType.reservedStatic.getTypeExplanation())){
                //toReturn = true;
                throw new SemanticException(SemanticErrorIIMessage.accessToAttributeInStaticContext(getToken()));
            }

            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme()) && s.getModifier() != null){
                //toReturn = true;
                //throw new SemanticException(SemanticErrorIIMessage.accessToAttributeInStaticContext(getToken()));
                tipo = n.getType();
            }
        }

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
                toReturn = true;
            }
            t = n.getName();
        }
        // toReturn = toReturn || st.getCurrentClass().getAttributes().contains(this.getToken().getLexeme());
        for(Element e : st.getCurrentClass().getAttributes().values()){
            Attribute n = (Attribute) e;
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme()) && s.getModifier() != null && s.getModifier().getLexeme().equals(TokenType.reservedStatic.getTypeExplanation())){
                //toReturn = true;
                throw new SemanticException(SemanticErrorIIMessage.accessToAttributeInStaticContext(getToken()));
            }
            t = n.getName();
        }
        if (toReturn){
            throw new SemanticException(SemanticErrorIIMessage.variableAlreadyExists(token));
        }
    }

    public Encadenado getLastEncadenado (){
        if (encadenado == null)
            return null;
        return encadenado.getLastEncadenado();
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

    public AbstractType getTipo() {
        return tipo;
    }

    public void setVar(Var v){ var = v; }
}
