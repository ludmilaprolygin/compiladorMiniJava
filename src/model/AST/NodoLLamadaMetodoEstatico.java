package model.AST;

import model.Token;
import model.symbolTable.*;
import model.symbolTable.Class;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;
import utils.messages.SemanticErrorIMessage;

import static model.TokenType.reservedStatic;

public class NodoLLamadaMetodoEstatico extends NodoExpresion{
    private Token idC;
    private Token idM;
    private java.util.List<NodoExpresion> argumentos;

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
        Method m = belongingClass.getMethods().get(tokenM);
        if (m == null)
            throw new SemanticException(SemanticErrorIIMessage.methodNotDeclared(idM));
        if (m.getModifier() != null && !m.getModifier().getTokenType().equals(reservedStatic)){
            throw new SemanticException(SemanticErrorIIMessage.methodNotStatic(idM));
        }
        compareArgs(belongingClass, idM);
        return m.getReturnType();
    }

    private void compareArgs(Class belongingClass, Token metodo) throws SemanticException {
        Token tokenM = belongingClass.getMethods().getTokenByName(metodo.getLexeme());
        Method m = belongingClass.getMethods().get(tokenM);
        if (m.getParameters().size() != argumentos.size())
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(metodo));
        for (int i = 0; i < argumentos.size(); i++) {
            AbstractType argType = argumentos.get(i).check();
            AbstractType paramType = ((Parameter) m.getParameters().get(i)).getType();
            try {
                argType.compatible(paramType);
            }
            catch (SemanticException e) {
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(metodo));
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
}
