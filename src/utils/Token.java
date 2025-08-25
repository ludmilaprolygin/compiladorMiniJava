package utils;

//TODO: relocate

public class Token {
    private final String tokenType;
    private final String lexeme;
    private final int row;
    public Token (String tokenType, String lexeme, int row) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.row = row;
    }

    public String format() {
        return "(" + tokenType + ","  + lexeme + "," + row + ")";
    }
}