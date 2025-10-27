package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.ClassType;
import model.symbolTable.SymbolTable;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import java.util.LinkedList;
import java.util.List;

import static model.TokenType.idClase;

public class NodoLLamadaConstructor extends NodoExpresion {
    protected ClassType classType;
    protected List<NodoExpresion> parametros;
    protected Encadenado encadenado;

    public NodoLLamadaConstructor(ClassType classType){
        this.classType = classType;
        parametros = new LinkedList<>();
    }

    public NodoLLamadaConstructor(ClassType classType, List<NodoExpresion> p){
        this.classType = classType;
        parametros = p;
    }

    @Override
    public AbstractType check() throws SemanticException {
        checkClassExistance();
        if(encadenado != null)
            return encadenado.check(classType);
        else
            return classType;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        if (parametros.isEmpty())
            return toReturn + "new " + classType.getName().getLexeme() + "() \n";
        else
            return toReturn + "new " + classType.getName().getLexeme() + "(" + parametros.toString()+ ")\n";
    }

    @Override
    public Token getToken() {
        return classType.getName();
    }

    public void setEncadenado(Encadenado e){
        encadenado = e;
    }

    public Encadenado getLastEncadenado (){
        if (encadenado == null)
            return null;
        return encadenado.getLastEncadenado();
    }

    public Encadenado getEncadenado() {
        return encadenado;
    }

    protected void checkClassExistance() throws SemanticException {
        if (classType == null) {
            throw new SemanticException(SemanticErrorIIMessage.undeclaredType(new Token(idClase, "", -1)));
        }
        else{
            Token t = SymbolTable.symbolTable().getClasses().getTokenByName(classType.getName().getLexeme());
            if (t == null) {
                throw new SemanticException(SemanticErrorIIMessage.undeclaredType(classType.getName()));
            }
        }
    }
}
