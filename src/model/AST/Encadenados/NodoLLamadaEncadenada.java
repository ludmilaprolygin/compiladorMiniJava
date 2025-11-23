package model.AST.Encadenados;

import model.AST.Expresiones.NodoExpresion;
import model.Token;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Comments;
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
        Method metodo = associatedMethod;
        int offset = metodo.getOffset();
        boolean isStatic = metodo.getModifier() != null && metodo.getModifier().getTokenType().equals(reservedStatic);
        boolean isVoid = metodo.getReturnType().getName().getLexeme().equals(reservedVoid.getTypeExplanation());

        if(!isVoid){
            o.gen(Instructions.RMEM + " 1" + Comments.RESERVE_RETURN.getComment());
            o.gen(Instructions.SWAP.toString());
        }

        for (NodoExpresion p : parametros) {
            p.gen(o);
            o.gen(Instructions.SWAP.toString());
        }

        if(!isStatic){
            o.gen(Instructions.DUP.toString() + Comments.DUP_THIS.getComment());
            o.gen(Instructions.LOADREF + " 0" + Comments.LOAD_VTABLE.getComment() + " (" + tipo.getName().getLexeme() + ")");
            o.gen(Instructions.LOADREF + " " + offset + Comments.LOAD_METHOD.getComment(metodo.getName().getLexeme()));
        }
        else{
            int a = parametros.size() + Integer.parseInt(CodeGenConfig.OFFSET_THIS);
            for(NodoExpresion p : parametros){
                o.gen(Instructions.SWAP.toString() + " " + Comments.MOVE_THIS.getComment());
            }
            o.gen(Instructions.POP.toString());
            o.gen(Instructions.PUSH + " lbl_" + nombre.getLexeme() + "@" + metodo.getCreator().getName().getLexeme());
        }

        o.gen(Instructions.CALL.toString());

        if (encadenado != null && !(encadenado instanceof EncadenadoVacio)) {
            encadenado.gen(o, associatedMethod.getReturnType());
        }
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
