package model.AST.Expresiones;

import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.Token;
import model.codeGeneration.CodeGenConfig;
import model.codeGeneration.Instructions;
import model.symbolTable.*;
import model.symbolTable.Class;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorIIMessage;

import java.util.LinkedList;
import java.util.List;

import static model.TokenType.idClase;
import static model.codeGeneration.CodeGenConfig.OFFSET_THIS;

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
        compareArgs(checkClassExistance());
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

    @Override
    public void gen(OutputManager o) {

        int CIRsize = 2; // 2 for VT and toString result
        try {
            CIRsize = checkClassExistance().getAttributes().size() + 2;
        } catch (SemanticException e) {}

        o.gen(Instructions.RMEM + " 1");

        o.gen(Instructions.PUSH + " " + CIRsize);
        o.gen(CodeGenConfig.PUSH_MALLOC);
        o.gen(Instructions.CALL.toString());

        o.gen(Instructions.DUP.toString());
        o.gen(Instructions.PUSH + " VT@" + classType.getName().getLexeme());
        o.gen(Instructions.STOREREF + " 0");

        o.gen(Instructions.DUP.toString());

        for(NodoExpresion n : parametros){
            n.gen(o);
            o.gen(Instructions.SWAP.toString());
        }

//        o.gen(Instructions.DUP.toString());
        o.gen(Instructions.PUSH + " lbl_builder@" + classType.getName().getLexeme());
        o.gen(Instructions.CALL.toString());

        if(encadenado != null && !(encadenado instanceof EncadenadoVacio)){
            encadenado.gen(o, classType);
        }
    }


    public Encadenado getEncadenado() {
        return encadenado;
    }

    protected Class checkClassExistance() throws SemanticException {
        System.out.println(getToken().getLexeme());
        System.out.println("Checking existence of class: " + classType.getName().getLexeme());
        for(MainElement e : SymbolTable.symbolTable().getCheckedElements()){
            System.out.println(e.getName().getLexeme());
        }
        System.out.println("--------------------------------");
        Class c = null;
        if (classType == null) {
            throw new SemanticException(SemanticErrorIIMessage.undeclaredType(new Token(idClase, "", -1)));
        }
        else{
            Token t = SymbolTable.symbolTable().getClasses().getTokenByName(getToken().getLexeme());
            c = SymbolTable.symbolTable().getClasses().get(t);
            if (t == null) {
                for(MainElement e : SymbolTable.symbolTable().getCheckedElements()){
                    //System.out.println(e.getName().getLexeme() + " - " + e.getClass().getSimpleName() + " " + classType.getName().getLexeme());
                    if(e instanceof Class){
                        Class classElement = (Class) e;
                        //System.out.println("Comparing between: " + classElement.getName().getLexeme() + " and " + classType.getName().getLexeme());
                        //System.out.println(classElement.getName().getLexeme().equals(getToken().getLexeme()));
                        if(classElement.getName().getLexeme().equals(getToken().getLexeme())){
                            c = classElement;
                            return c;
                        }
                    }
                }
                throw new SemanticException(SemanticErrorIIMessage.undeclaredType(classType.getName()));
            }
        }
        return c;
    }

    private void compareArgs(Class belongingClass) throws SemanticException {
        if(!belongingClass.getBuilderTable().isEmpty()){
            Builder b = (Builder) belongingClass.getBuilderTable().getFirst();
            if (b.getParameters().size() != parametros.size())
                throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(classType.getName()));
            for (int i = 0; i < parametros.size(); i++) {
                AbstractType argType = parametros.get(i).check();
                AbstractType paramType = ((Parameter) b.getParameters().get(i)).getType();
                try {
                    argType.compatible(paramType);
                }
                catch (SemanticException e) {
                    throw new SemanticException(SemanticErrorIIMessage.incompatibleTypes(classType.getName()));
                }
            }
        }
    }
}
