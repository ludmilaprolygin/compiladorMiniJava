package utils.messages;

import model.Token;
import model.symbolTable.Method;

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

    public static String constructorDoesNotExist(Token name) {
        return basicErrorInit(name) + ": Constructor " + name.getLexeme() + " incorrect; class does not exist in this scope \n" + basicErrorEnd(name);
    }

    public static String constructorFoundInInterface(Token n) {
        return basicErrorInit(n) + ": Constructor " + n.getLexeme() + " found in interface; constructors are not allowed in interfaces \n" + basicErrorEnd(n);
    }

    public static String parameterAlreadyExists(Token n) {
        return basicErrorInit(n) +  ": Parameter " + n.getLexeme() + " already exists \n" + basicErrorEnd(n);
    }

    public static String circularHierarchy(Token inheritance) {
        return basicErrorInit(inheritance) + ": Circular hierarchy due to extension of " + inheritance.getLexeme() + "\n" + basicErrorEnd(inheritance);
    }

    public static String methodDoesNotOverrideCorrectly(Method method) {
        return basicErrorInit(method.getName()) + ": Method " + method.toString() + " does not override correctly \n" + basicErrorEnd(method.getName());
    }

    public static String cannotDeclareAbstractMethod(Token t) {
        return basicErrorInit(t) + ": Cannot declare abstract method " + t.getLexeme() + " from a class that is not abstract \n" + basicErrorEnd(t);
    }
}
