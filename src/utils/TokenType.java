package utils;

// https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html

public enum TokenType {
    END_OF_FILE     ("end of file"),
    idClase         ("idClase"),
    idMetVar        ("idMetVar"),
    intLiteral      ("intLiteral"),
    charLiteral     ("charLiteral"),
    stringLiteral   ("stringLiteral"),
    divOp           ("divOp ~ /");

    private final String typeExplanation;

    TokenType(String typeExplanation) {
        this.typeExplanation = typeExplanation;
    }

    public String getTypeExplanation() {
        return typeExplanation;
    }
}