package model.AST.Encadenados;

import model.AST.Expresiones.NodoExpresion;
import model.Token;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import java.util.LinkedList;
import java.util.List;

import static model.TokenType.reservedStatic;
import static model.TokenType.reservedVoid;

public class NodoLLamadaEncadenada extends Encadenado {
    protected List<NodoExpresion> parametros;
    protected Method associatedMethod;
    public NodoLLamadaEncadenada(Token t){
        super(t);
        parametros = new LinkedList<>();
    }
    public NodoLLamadaEncadenada(Token t, Encadenado e){
        super(t, e);
        parametros = new LinkedList<>();
    }
    public NodoLLamadaEncadenada(Token t, Encadenado e, List<NodoExpresion> p){
        super(t, e);
        parametros = p;
    }
    public void setEncadenado(Encadenado encadenado) {
        this.encadenado = encadenado;
    }
    public void setParametros(List<NodoExpresion> p){
        parametros = p;
    }
    public List<NodoExpresion> getParametros(){
        return parametros;
    }
    @Override
    public AbstractType check(AbstractType t) throws SemanticException {
        if(t.isPrimitive() || t instanceof VoidType)
            throw new SemanticException(SemanticErrorIIMessage.primitiveTypesCantReceiveCalls(nombre));

        Token token = SymbolTable.symbolTable().getClasses().getTokenByName(t.getName().getLexeme());
        model.symbolTable.Class c = SymbolTable.symbolTable().getClasses().get(token);

        if(c != null) {
            model.symbolTable.List methods = c.getMethods();

            for(Element e : methods.values()){
                Method method = (Method) e;
                if(method.getName().getLexeme().equals(nombre.getLexeme())){
                    compareArgs(method);

                    AbstractType methodType = method.getReturnType();
                    if(encadenado == null) {
                        encadenado = new EncadenadoVacio();
                        return methodType;
                    }

                    return encadenado.check(methodType);
                }
            }
            throw new SemanticException(SemanticErrorIIMessage.methodNotDeclared(nombre));
        }

        throw new SemanticException(SemanticErrorIIMessage.undeclaredType(t.getName()));
    }

    @Override
    public void gen(OutputManager o, AbstractType tipo) {
        for (NodoExpresion n : parametros) {
            n.gen(o);
        }

        boolean retornaValor =
                !associatedMethod.getReturnType().getName().getLexeme()
                        .equals(reservedVoid.getTypeExplanation());

        if (associatedMethod.getModifier() != null &&
                associatedMethod.getModifier().getLexeme()
                        .equals(reservedStatic.getTypeExplanation())) {

             o.gen(Instructions.POP.toString());

             o.gen(Instructions.PUSH + " lbl_"
                    + associatedMethod.getName().getLexeme()
                    + "@" + associatedMethod.getCreator().getName().getLexeme());
        }
        else {
//             o.gen(Instructions.LOADREF + " 0");
//             o.printStackTop();
             o.gen(Instructions.PUSH + " VT@" + tipo.getName().getLexeme());
             o.printStackTop();
             o.gen(Instructions.LOADREF + " " + associatedMethod.getOffset());
             o.printStackTop();
        }

        o.gen(Instructions.CALL.toString());
        o.printStackTop();
//        if (retornaValor) {
//            o.gen(Instructions.DUP.toString());
//        }

        if (encadenado != null && !(encadenado instanceof EncadenadoVacio)) {
            encadenado.gen(o, associatedMethod.getReturnType());
        }
        //o.gen(Instructions.FMEM + " 1");
    }


    private void compareArgs(Method m) throws SemanticException {
        if (m.getParameters().size() != parametros.size())
            throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(nombre));

        for (int i = 0; i < parametros.size(); i++) {

            AbstractType argType = parametros.get(i).check();

            AbstractType paramType = ((Parameter) m.getParameters().get(i)).getType();

            try {
                paramType.compatible(argType);
            }
            catch (SemanticException e) {
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(nombre));
            }
        }

        associatedMethod = m;
    }
}
