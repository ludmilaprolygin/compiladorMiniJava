package model.AST.Operandos;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.AST.Sentencias.NodoBloque;
import model.Token;
import model.symbolTable.*;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoVar extends NodoOperando {
    protected AbstractType tipo;
    protected Encadenado encadenado;
    protected boolean isDeclared;
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
        Token t = null;
        for(Element n : st.getCurrentService().getParameters()){
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme())){
                toReturn = true;
            }
            t = n.getName();
        }
        toReturn = toReturn || st.getCurrentClass().getAttributes().contains(this.getToken().getLexeme());
        for(Attribute n : st.getCurrentClass().getAttributes().values()){
            if(n.getName() != null && this.getToken() != null && n.getName().getLexeme().equals(this.getToken().getLexeme())){
                toReturn = true;
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

        // Obtenemos el último eslabón de la cadena
        Encadenado ultimo = this.getLastEncadenado();

        // Caso 1: 'x.y.miMetodo() = 10'
        // Si el último eslabón es una llamada, es un error.
        if (ultimo instanceof NodoLLamadaEncadenada) {
            throw new SemanticException(SemanticErrorIIMessage.invalidLeftValue(ultimo.getNombre()));
        }

        // Caso 2: 'x = 10' (ultimo == null)
        // Caso 3: 'x.y.miAtributo = 10' (ultimo es NodoVarEncadenada)
        // En ambos casos, es un L-Value válido.

        // Como es válido, simplemente ejecutamos el 'check()' normal
        // para obtener el tipo final del lado izquierdo.
        return this.check();
    }
}
