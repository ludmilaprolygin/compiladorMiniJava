package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.Token;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

public class NodoLLamadaMetodo extends NodoExpresion {
    protected Token metodo;
    protected java.util.List<NodoExpresion> argumentos;
    protected model.symbolTable.MainElement belongingClass;
    protected Encadenado encadenado;

    public NodoLLamadaMetodo(Token m, java.util.List<NodoExpresion> a, model.symbolTable.MainElement c){
        metodo = m;
        argumentos = a;
        belongingClass = c;
    }

    public Token getMetodo() { return metodo; }

    @Override
    public AbstractType check() throws SemanticException {
        Token tokenM = belongingClass.getMethods().getTokenByName(metodo.getLexeme());
        Method m = (Method) belongingClass.getMethods().get(tokenM);

        if (m == null)
            throw new SemanticException(SemanticErrorIIMessage.methodNotDeclared(metodo));

        Service servicioActual = SymbolTable.symbolTable().getCurrentService();

        if (servicioActual instanceof Method && ((Method) servicioActual).isStatic() && !m.isStatic()) {
            throw new SemanticException(SemanticErrorIIMessage.thisInStaticContext(metodo));
        }
        compareArgs();
        AbstractType toReturn = m.getReturnType();

        if(encadenado != null)
            if(m.getReturnType() instanceof ClassType || m.getReturnType() instanceof UniversalType)
                toReturn = encadenado.check(m.getReturnType());
                //encadenado.check(tipo);
            else
                throw new SemanticException(SemanticErrorIIMessage.primitiveTypesCantReceiveCalls(metodo));

        return toReturn;
    }

    @Override
    public String toString(int depth) {
        String toReturn = "";
        for (int i = 0; i < depth; i++)
            toReturn += "- ";
        if (argumentos.isEmpty())
            return toReturn + metodo.getLexeme() + "() \n";
        else
            return toReturn + metodo.getLexeme() + "(" + argumentos.toString()+ ")\n";
    }

    @Override
    public Token getToken() {
        return metodo;
    }

    public void setEncadenado(Encadenado e){ encadenado = e; }

    private void compareArgs() throws SemanticException {
        Token tokenM = belongingClass.getMethods().getTokenByName(metodo.getLexeme());
        Method m = (Method) belongingClass.getMethods().get(tokenM);
        if (m.getParameters().size() != argumentos.size())
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(metodo));
        for (int i = 0; i < argumentos.size(); i++) {
            AbstractType argType = argumentos.get(i).check();
            AbstractType paramType = ((Parameter) m.getParameters().get(i)).getType();
            System.out.println(argType + " " + paramType);
            try {
                paramType.compatible(argType);
            }
            catch (SemanticException e) {
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(metodo));
            }
        }
    }

    public AbstractType checkLeftValue() throws SemanticException {
        Encadenado ultimo = this.getLastEncadenado();

        if (ultimo == null) {
            throw new SemanticException(SemanticErrorIIMessage.invalidLeftValue(this.getToken()));
        }

        if (ultimo instanceof NodoLLamadaEncadenada) {
            throw new SemanticException(SemanticErrorIIMessage.invalidLeftValue(ultimo.getNombre()));
        }
        return this.check();
    }

    public Encadenado getLastEncadenado (){
        if (encadenado == null)
            return null;
        return encadenado.getLastEncadenado();
    }

    @Override
    public void gen(OutputManager o) {
        Token tokenM = belongingClass.getMethods().getTokenByName(metodo.getLexeme());
        Method m = (Method) belongingClass.getMethods().get(tokenM);

        for(NodoExpresion n : argumentos){
            n.gen(o);
        }
        o.gen(Instructions.PUSH + " lbl_" + metodo.getLexeme() + "@" + m.getCreator().getName().getLexeme());
        o.gen(Instructions.CALL.toString());
    }

    public Encadenado getEncadenado() {
        return encadenado;
    }
}
