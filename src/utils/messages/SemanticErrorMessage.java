package utils.messages;

import model.Token;

public class SemanticErrorMessage {
    public static String attributeAlreadyExists(Token t) {
        return basicErrorInit(t) + ": Attribute " + t.getLexeme() + " already exists \n" + basicErrorEnd(t);
    }

    public static String undeclaredType(Token t) {
        return basicErrorInit(t) + ": Type " + t.getLexeme() + " is not declared \n" + basicErrorEnd(t);
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

    public static String interfaceAlreadyExists(Token t) {
        return basicErrorInit(t) + ": Interface " + t.getLexeme() + " already exists \n" + basicErrorEnd(t);
    }

    public static String parentDoesNotExist(Token t) {
        return basicErrorInit(t) + ": The identifier of the parent " + t.getLexeme() + " could not be found \n" + basicErrorEnd(t);
    }

    public static String interfaceExtendingAClass(Token t) {
        return basicErrorInit(t) + ": An interface cannot extend a class, but " + t.getLexeme() + " is a class \n" + basicErrorEnd(t);
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

    public static String parametricTypeNotAllowed(Token name) {
        return basicErrorInit(name) + ": Parametric types are only allowed for user-defined types, but " + name.getLexeme() + " is a primitive type \n" + basicErrorEnd(name);
    }
}
