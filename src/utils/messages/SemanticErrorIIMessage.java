package utils.messages;

import model.Token;
import model.symbolTable.AbstractType;
import model.symbolTable.Builder;
import model.symbolTable.Method;

public class SemanticErrorIIMessage {
    private static String basicErrorInit(Token t) {
        try {
            return "Error semántico en linea " + t.getRow();
        }
        catch(Exception e) {e.printStackTrace();}
        return null;
    }

    private static String basicErrorEnd(Token t) {
        return "[Error:" + t.getLexeme() + "|" + t.getRow() + "]";
    }

    public static String variableAlreadyExists(Token n) {
        return basicErrorInit(n) +  ": Variable " + n.getLexeme() + " already exists \n" + basicErrorEnd(n);
    }
    public static String missingReturnStatement(Token name) {
        return basicErrorInit(name) + ": Missing return statement in method " + name.getLexeme() + " with non-void return type \n" + basicErrorEnd(name);
    }

    public static String undeclaredType(Token t) {
        return basicErrorInit(t) + ": Type " + t.getLexeme() + " is not declared \n" + basicErrorEnd(t);
    }

    public static String incompatibleTypes(Token t) {
        return basicErrorInit(t) + ": Incompatibles types\n" + basicErrorEnd(t);
    }

    public static String composabilityNotAllowed(Token operador) {
        return basicErrorInit(operador) + ": Left side of assignment error\n" + basicErrorEnd(operador);
    }

    public static String variableDoesNotExist(Token token) {
        return basicErrorInit(token) + ": Variable not declared\n" + basicErrorEnd(token);
    }

    public static String missingMainMethod() {
        Token t = new Token(null, "", -1);
        return basicErrorInit(t) + ": Missing main method\n" + basicErrorEnd(t);
    }

    public static String methodNotDeclared(Token metodo) {
        return basicErrorInit(metodo) + ": Method " + metodo.getLexeme() + " is not declared \n" + basicErrorEnd(metodo);
    }

    public static String methodNotStatic(Token idM) {
        return basicErrorInit(idM) + ": Method " + idM.getLexeme() + " is not static \n" + basicErrorEnd(idM);
    }

    public static String primitiveTypesCantReceiveCalls(AbstractType tipo) {
        return basicErrorInit(tipo.getName()) + ": Message not supported on " + tipo.getName().getLexeme() + " type\n" + basicErrorEnd(tipo.getName());
    }

    public static String firstOperandMustBeBoolean(Token token) {
        return basicErrorInit(token) + ": First operand of ternary operator must be boolean\n" + basicErrorEnd(token);
    }

    public static String optionsMustBeCompatibleWithOperandType(Token name) {
        return basicErrorInit(name) + ": Both options of ternary operator must be compatible with the operand type\n" + basicErrorEnd(name);
    }

    public static String voidMethodWithReturnStatement(Token name) {
        return basicErrorInit(name) + ": Void method " + name.getLexeme() + " cannot have a return statement \n" + basicErrorEnd(name);
    }

    public static String incorrectReturnType(Token token) {
        return basicErrorInit(token) + ": Return type is not compatible with method's declared return type \n" + basicErrorEnd(token);
    }

    public static String incorrectReturnType(Token token, String s) {
        return basicErrorInit(token) + ": Return type is not compatible with method's declared return type \n [Error:" + s + "|" + token.getRow() + "]";
    }

    public static String thisInStaticContext(Token token) {
        return basicErrorInit(token) + ": 'this' cannot be used in a static context \n" + basicErrorEnd(token);
    }

    public static String thisInStaticContext(Token token, String s) {
        return basicErrorInit(token) + ": 'this' cannot be used in a static context \n [Error:this|" + token.getRow() + "]";
    }
}