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
        boolean retornaValor =
                !associatedMethod.getReturnType().getName().getLexeme()
                        .equals(reservedVoid.getTypeExplanation());

        // Se asume: al entrar, la referencia al receptor (CIR) está en el tope de la pila.

        // 1) reservar slot de retorno SI corresponde (vacío)
        if (retornaValor) {
            o.gen(Instructions.RMEM + " 1");
        }

        // 2) generar parámetros intercalando SWAP según la convención CeIVM
        //    (SWAP antes de cada parámetro para dejar el receptor en la posición adecuada)
        for (NodoExpresion n : parametros) {
            o.gen(Instructions.SWAP.toString()); // mueve receptor hacia donde debe quedar
            n.gen(o);                            // genera el parámetro (ahora apilado)
        }

        // 3) duplicar la referencia al receptor para poder acceder a su VT
        o.gen(Instructions.DUP.toString());

        // 4) cargar VT y dirección del método y CALL
        o.gen(Instructions.LOADREF + " 0");
        o.gen(Instructions.LOADREF + " " + associatedMethod.getOffset());

        //o.printStackTop();

        o.gen(Instructions.CALL.toString());

        // 5) si hay encadenado, seguir
        if (encadenado != null && !(encadenado instanceof EncadenadoVacio)) {
            encadenado.gen(o, associatedMethod.getReturnType());
        }

        // 6) limpieza / conversión del tipo de retorno si corresponde
        //generateReturnType(o);
    }



    public void generateReturnType(OutputManager o) {
        AbstractType tipo = associatedMethod.getReturnType();
        if (tipo == null) return;
        if (!tipo.getName().getLexeme().equals(reservedVoid.getTypeExplanation())) return;
//        if (!(tipo instanceof ClassType)){
//            o.gen(Instructions.LOADREF + " 1");
//        }
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
