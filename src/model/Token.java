package model;

//TODO: relocate

public class Token {
    private final TokenType tokenType;
    private final String lexeme;
    private final int row;

    public Token (TokenType tokenType, String lexeme, int row) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.row = row;
    }

    public TokenType getTokenType () { return tokenType; }

    public String format() {
        return "(" + tokenType.getTypeExplanation() + ","  + lexeme + "," + row + ")";
    }
}