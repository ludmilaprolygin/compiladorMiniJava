package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Operandos.NodoVar;
import model.Token;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import model.symbolTable.Class;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;
import utils.messages.SemanticErrorIMessage;

import static model.TokenType.reservedStatic;

public class NodoLLamadaMetodoEstatico extends NodoExpresion {
    private Token idC;
    private Token idM;
    private java.util.List<NodoExpresion> argumentos;
    private Encadenado encadenado;

    public NodoLLamadaMetodoEstatico(Token c, Token m, java.util.List<NodoExpresion> args){
        idC = c;
        idM = m;
        argumentos = args;
    }

    @Override
    public AbstractType check() throws SemanticException {
        Class belongingClass = null;
        for(Class c : SymbolTable.symbolTable().getClasses().values()){
            if(c.getName().getLexeme().equals(idC.getLexeme())){
                belongingClass = c;
                break;
            }
        }
        if(belongingClass == null)
            throw new SemanticException(SemanticErrorIMessage.undeclaredType(idC));

        Token tokenM = belongingClass.getMethods().getTokenByName(idM.getLexeme());
        Method m = (Method) belongingClass.getMethods().get(tokenM);

        if (m == null)
            throw new SemanticException(SemanticErrorIIMessage.methodNotDeclared(idM));

        if (m.getModifier() == null || !m.getModifier().getTokenType().equals(reservedStatic)){
            throw new SemanticException(SemanticErrorIIMessage.methodNotStatic(idM));
        }

        compareArgs(m);

        AbstractType miTipoDeRetorno = m.getReturnType();

        if (encadenado != null) {
            return encadenado.check(miTipoDeRetorno);
        } else {
            return miTipoDeRetorno;
        }
    }

    private void compareArgs(Method m) throws SemanticException {
        if (m.getParameters().size() != argumentos.size())
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(idM)); // Usamos idM como token de error

        for (int i = 0; i < argumentos.size(); i++) {
            AbstractType argType = argumentos.get(i).check();
            AbstractType paramType = ((Parameter) m.getParameters().get(i)).getType();

            try {
                argType.compatible(paramType);
            }
            catch (SemanticException e) {
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(idM));
            }
        }
    }

    @Override
    public String toString(int depth) {
        return "";
    }

    @Override
    public Token getToken() {
        return idC;
    }

    @Override
    public Encadenado getLastEncadenado (){
        if (encadenado == null)
            return null;
        return encadenado.getLastEncadenado();
    }

    public Encadenado getEncadenado(){ return encadenado; }

    @Override
    public void gen(OutputManager o) {
        for(NodoExpresion n : argumentos){
            n.gen(o);
        }
        o.gen(Instructions.PUSH + " lbl_" + idM.getLexeme() + "@" + idC.getLexeme());

        //o.printStackTop();

        o.gen(Instructions.CALL.toString());
    }

    public void setEncadenado(Encadenado chain) {
        encadenado = chain;
    }
}
