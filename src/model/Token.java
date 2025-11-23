package model;

public class Token {
    private final TokenType tokenType;
    private String lexeme;
    private final int row;

    public Token (TokenType tokenType, String lexeme, int row) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.row = row;
    }

    public TokenType getTokenType () { return tokenType; }

    public int getRow() { return row; }
    public String getLexeme() { return lexeme; }

    public String format() {
        return "(" + tokenType.getTypeExplanation() + ","  + lexeme + "," + row + ")";
    }
    public void setLexeme(String lexeme) { this.lexeme = lexeme; }
}