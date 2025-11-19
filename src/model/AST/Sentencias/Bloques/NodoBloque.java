package model.AST.Sentencias.Bloques;

import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Expresiones.NodoExpresion;
import model.AST.Expresiones.NodoExpresionBinaria;
import model.AST.Expresiones.NodoLLamadaMetodo;
import model.AST.Operandos.NodoOperando;
import model.AST.Operandos.NodoVar;
import model.AST.Sentencias.NodoReturn;
import model.AST.Sentencias.NodoSentencia;
import model.AST.Sentencias.NodoSentenciaConExpresion;
import model.codeGeneration.Instructions;
import model.symbolTable.AbstractType;
import model.symbolTable.Method;
import model.symbolTable.Var;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import java.util.LinkedList;
import java.util.List;

import static model.symbolTable.SymbolTable.symbolTable;

public class NodoBloque extends NodoSentencia {

    protected final List<NodoSentencia> statements;
    protected List<NodoOperando> variables;
    protected NodoBloque bloqueContenedor;
    protected List<NodoLLamadaMetodo> llamadas;

    public NodoBloque(NodoBloque bc) {
        statements = new LinkedList<>();
        variables = new LinkedList<>();
        llamadas = new LinkedList<>();
        bloqueContenedor = bc;
    }

    public List<NodoSentencia> getStatements() { return statements; }
    public List<NodoOperando> getVariables() { return variables; }
    public void addVariable(NodoOperando variable) throws SemanticException {
        for(NodoOperando o : variables){
            if(o.getToken().getLexeme().equals(variable.getToken().getLexeme()))
                throw new SemanticException(SemanticErrorIIMessage.variableAlreadyExists(variable.getToken()));
        }
        NodoBloque bloqueC = bloqueContenedor;
        while(bloqueC != null){
            for(NodoOperando o : bloqueC.getVariables()){
                if(o.getToken().getLexeme().equals(variable.getToken().getLexeme()))
                    throw new SemanticException(SemanticErrorIIMessage.variableAlreadyExists(variable.getToken()));
            }
            bloqueC = bloqueC.getBloqueContenedor();
        }
        variables.addLast(variable);
    }

    public void setBloqueContenedor(NodoBloque bloque) {
        this.bloqueContenedor = bloque;
    }

    public NodoBloque getBloqueContenedor() {
        return bloqueContenedor;
    }

    public void addStatement (NodoSentencia statement) {
        statements.add(statement);
    }
    public void check() throws SemanticException {
        NodoBloque bloqueAnterior = symbolTable().getBloque();
        symbolTable().setBloque(this);
        AbstractType retType = null;
        NodoReturn r = null;
        for(NodoSentencia s : statements)
        {
            s.check();
            if(s instanceof NodoReturn)
            {
                r = ((NodoReturn) s);
                retType = r.getType();
            }
            if(s instanceof NodoSentenciaConExpresion e && (e.expresion instanceof NodoExpresionBinaria || (e.expresion instanceof NodoVar && (((NodoVar) e.expresion).getEncadenado() == null || ((NodoVar) e.expresion).getEncadenado() instanceof EncadenadoVacio)))){
                throw new SemanticException(SemanticErrorIIMessage.expresionMislocated(e.expresion.getToken()));
            }
        }
        if(r!=null && retType != null && symbolTable().getCurrentService() instanceof Method){
            try{
                r.compatibleWithType(retType);
                //retType.compatible(((Method) symbolTable().getCurrentService()).getReturnType());
            }
            catch(Exception e){
                throw new SemanticException(SemanticErrorIIMessage.incorrectReturnType(retType.getName(), "return"));
            }
        }
        symbolTable().setBloque(bloqueAnterior);
    }

    public String toString(int depth){
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "  ";
        toReturn += "{\n";
        for(NodoSentencia s : statements){
            toReturn += s.toString(depth + 1) + "\n";
        }
        for (int i = 0; i < depth; i++)
            toReturn += "  ";
        toReturn += "}";
        return toReturn;
    }

    public NodoReturn hasReturnStatementSomewhere() throws SemanticException {
        NodoReturn toReturn;
        for (NodoSentencia s : statements) {
            toReturn = s.hasReturnStatementSomewhere();
            if(toReturn != null){
                if(statements.getLast() != s){
                    throw new SemanticException(SemanticErrorIIMessage.deadCodeDetected(toReturn.getToken()));
                }
                return toReturn;
            }
        }
        return null;
    }

    @Override
    public void gen(OutputManager o) {


        for (NodoSentencia s : statements) {
            s.gen(o);
        }

    }


    public void addLlamada(NodoExpresion toReturn) {
        llamadas.add((NodoLLamadaMetodo) toReturn);
    }

    public void checkThisOnStaticContext() throws SemanticException {
        for(NodoSentencia s : statements){
            if(s instanceof NodoBloque b){
                b.checkThisOnStaticContext();
            } else
                s.checkThisOnStaticContext();

        }
    }

    public NodoSentencia getLastStatement(){
        return statements.getLast();
    }

    private void setOffsets(){
        int offset = 0;
        for(NodoOperando o : variables){
            if(o instanceof NodoVar v){
                v.setOffset(offset);
                offset--;
            }
        }
    }
}
