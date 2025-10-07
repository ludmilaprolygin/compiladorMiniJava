package utils.messages;

import model.Token;
import model.symbolTable.Builder;
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
        return basicErrorInit(t) + ": Cannot declare abstract method in a class that is not abstract \n" + basicErrorEnd(t);
    }

    public static String builderFoundInAbstractClass(Token name) {
        return basicErrorInit(name) + ": Constructor " + name.getLexeme() + " found in abstract class; constructors are not allowed in abstract classes \n" + basicErrorEnd(name);
    }

    public static String builderDoesNotOverrideCorrectly(Builder myBuilder) {
        return basicErrorInit(myBuilder.getName()) + ": Constructor " + myBuilder.toString() + " does not override correctly \n" + basicErrorEnd(myBuilder.getName());
    }

    public static String missingReturnStatement(Token name) {
        return basicErrorInit(name) + ": Missing return statement in method " + name.getLexeme() + " with non-void return type \n" + basicErrorEnd(name);
    }

    public static String abstractMethodWithBody(Token name) {
        return basicErrorInit(name) + ": Abstract method " + name.getLexeme() + " cannot have a body \n" + basicErrorEnd(name);
    }

    public static String cannotExtendFromFinalClass(Token name) {
        return basicErrorInit(name) + ": Cannot extend from final class " + name.getLexeme() + " \n" + basicErrorEnd(name);
    }

    public static String abstractClassExtendsConcreteClass(Token name) {
        return basicErrorInit(name) + ": Abstract class " + name.getLexeme() + " cannot extend from a concrete class \n" + basicErrorEnd(name);
    }

    public static String missingImplementationOnAbstracMethod(Method m) {
        return basicErrorInit(m.getName()) + ": Missing implementation on abstract method " + m.toString() + " \n" + basicErrorEnd(m.getName());
    }

    public static String abstractMethodRedefinedAsAbstract(Method m) {
        return basicErrorInit(m.getName()) + ": Abstract method " + m.toString() + " cannot be redefined as abstract \n" + basicErrorEnd(m.getName());
    }

    public static String cannotOverrideFinalMethod(Method m) {
        return basicErrorInit(m.getName()) + ": Cannot override final method " + m.toString() + " \n" + basicErrorEnd(m.getName());
    }

    public static String cannotExtendFromStaticClass(Token name) {
        return basicErrorInit(name) + ": Cannot extend from static class " + name.getLexeme() + " \n" + basicErrorEnd(name);
    }

    public static String parametricTypeMustBeClass(Token name) {
        return basicErrorInit(name) + ": Parametric type " + name.getLexeme() + " must be a user-defined class \n" + basicErrorEnd(name);
    }

    public static String parametricInheritanceMismatch(Token name) {
        return basicErrorInit(name) + ": Parent " + name.getLexeme() + " is parametric but child doesn't have the same parametric type \n" + basicErrorEnd(name);
    }

    public static String interfaceCannotExtendClass(Token name) {
        return basicErrorInit(name) + ": Interface cannot extend class " + name.getLexeme() + " \n" + basicErrorEnd(name);
    }

    public static String mainElementCannotBeStatic(Token modifier) {
        return basicErrorInit(modifier) + ": Main element cannot be declared static \n" + basicErrorEnd(modifier);
    }

    public static String classesMustImplementAllInterfaceMethods(Token m) {
        return basicErrorInit(m) + ": Classes that implement interfaces must implement all parent interface methods \n" + basicErrorEnd(m);
    }
}