package utils.messages;

import model.Token;

public class SemanticErrorMessage {
    public static String attributeAlreadyExists(Token t) {
        return basicErrorInit(t) + ": Attribute " + t.getLexeme() + " already exists \n" + basicErrorEnd(t);
    }
    public static String methodAlreadyExists(Token t) {
        return basicErrorInit(t) + ": Method " + t.getLexeme() + " already exists \n" + basicErrorEnd(t);
    }

    public static String constructorAlreadyExists(Token t) {
        return basicErrorInit(t) + ": Constructor " + t.getLexeme() + " already exists \n" + basicErrorEnd(t);
    }

    public static String classAlreadyExists(Token t) {
        return basicErrorInit(t) + ": Class " + t.getLexeme() + " already exists \n" + basicErrorEnd(t);
    }

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
}
