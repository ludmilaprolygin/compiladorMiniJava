package utils.exceptions;

import model.Token;

public class SyntacticException extends Exception {
    public SyntacticException(Token currentToken, String tokenName) {}
}
