package utils.messages;

import model.Token;
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
        return basicErrorInit(t) + ": Incompatibles types" + basicErrorEnd(t);
    }
}