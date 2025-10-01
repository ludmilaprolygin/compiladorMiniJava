package model.symbolTable;

import model.Token;
import model.TokenType;
import utils.exceptions.SemanticException;
import utils.messages.SemanticErrorMessage;

import static model.symbolTable.SymbolTable.symbolTable;

public class Class extends MainElement {
    private Table<Constructor> constructors;
    public Class(Token m, Token n, Type t, Token i) {
        super(m, n, t, i);
        constructors = new Table<>();
    }

    public void addConstructor(Token t, Service s) throws SemanticException {
        Constructor c = (Constructor) s;
        if(!constructors.contains(t.getLexeme()))
            constructors.put(t, c);
        else {
            throw new SemanticException(SemanticErrorMessage.constructorAlreadyExists(t));
        }
    }

    public void addMethod(Token t, Service s) throws SemanticException {
        Method m = (Method) s;
        super.addMethod(t, m);
    }

    public Table<Constructor> getConstructors() { return constructors; }

    @Override
    public void correctDeclaration() throws SemanticException {
        String parentLexeme = inheritance != null ? inheritance.getLexeme() : null;
        if(inheritance != null && !symbolTable().getClasses().contains(parentLexeme)) {
            throw new SemanticException(SemanticErrorMessage.parentDoesNotExist(inheritance));
        }
        else if(inheritance != null && parentLexeme.equals(name.getLexeme())) {
            throw new SemanticException(SemanticErrorMessage.circularHierarchy(inheritance));
        }
        else if (inheritance != null && symbolTable().getClassHeriarchy().isAncestor(name.getLexeme(), parentLexeme)) {
            throw new SemanticException(SemanticErrorMessage.circularHierarchy(inheritance));
        }
        for (Attribute a : attributes.values())
            a.correctDeclaration();
        for (Method m : methods.values())
            m.correctDeclaration();
        for (Constructor c : constructors.values())
            c.correctDeclaration();
    }

    public String toString() {
        String toReturn = super.toString() + "\n" +
                "   Constructors: " + constructors.toString() + "\n";
        return toReturn;
    }
}
