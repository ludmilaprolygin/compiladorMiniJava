package lexicalAnalyzer;

import java.io.IOException;

import sourceManager.SourceManager;

import utils.format.Token;
import utils.format.TokenType;
import utils.messages.LexicalErrorMessage;
import utils.messages.GenericErrorMessage;

import utils.exceptions.LexicalException;

import static utils.format.TokenType.*;

public class LexicalAnalyzer {
    private static final int MAX_DIGITS = 9; 
    private static final int MAX_UNICODE_DIGITS = 4;
    private static final char ENTER = '\r';
    private static final char NEW_LINE = '\n';
 
    
    private SourceManager sourceManager;
    private char currentChar;
    private int row;
    private String lexeme;
    
    public LexicalAnalyzer(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
        lexeme = "";
    }

    public Token nextToken() throws LexicalException {
        lexeme = "";
        updateCurrentChar();
        return e0();
    }

    private void updateLexeme() {
        lexeme = lexeme + currentChar;
    }

    private void updateCurrentChar() {
        try {
            currentChar = sourceManager.getNextChar();
            row = sourceManager.getLineNumber();
        } catch (IOException exception) {
            System.out.println(GenericErrorMessage.FILE_READ_ERROR);
        }
    }

    private Token e0() throws LexicalException {
        if (Character.isWhitespace(currentChar)) {
            updateCurrentChar();
            return e0();
        }
        else if (Character.isLetter(currentChar)) {
            if (Character.isUpperCase(currentChar)) {
                updateLexeme();
                updateCurrentChar();
                return idClase();
            }
            else {
                updateLexeme();
                updateCurrentChar();
                return idMetVar();
            }
        }
        else if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return intLiteral();
        }
        else if (currentChar == '\'') {
            updateLexeme();
            updateCurrentChar();
            return charLiteral();
        }
        else if (currentChar == '"') {
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        }
        else if (currentChar == '/') {
            updateLexeme();
            updateCurrentChar();
            return slashSymbol();
        }
        else if (currentChar == '>') {
            updateLexeme();
            updateCurrentChar();
            return greaterSymbol();
        }
        else if (currentChar == '<') {
            updateLexeme();
            updateCurrentChar();
            return lesserSymbol();
        }
        else if (currentChar == '!') {
            updateLexeme();
            updateCurrentChar();
            return notSymbol();
        }
        else if (currentChar == '=') {
            updateLexeme();
            updateCurrentChar();
            return equalSymbol();
        }
        else if (currentChar == '&') {
            updateLexeme();
            updateCurrentChar();
            return ampersandSymbol();
        }
        else if (currentChar == '|') {
            updateLexeme();
            updateCurrentChar();
            return pipeSymbol();
        }
        else if (currentChar == '%') {
            updateLexeme();
            return new Token(modOp, lexeme, row);
        }
        else if (currentChar == '+') {
            updateLexeme();
            updateCurrentChar();
            return plusSymbol();
        }
        else if (currentChar == '-') {
            updateLexeme();
            updateCurrentChar();
            return minusSymbol();
        }
        else if (currentChar == '*') {
            updateLexeme();
            return new Token(multOp, lexeme, row);
        }
        else if (currentChar == '(') {
            updateLexeme();
            return new Token(openParenthesis, lexeme, row);
        }
        else if (currentChar == ')') {
            updateLexeme();
            return new Token(closeParenthesis, lexeme, row);
        }
        else if (currentChar == '{') {
            updateLexeme();
            return new Token(openBracket, lexeme, row);
        }
        else if (currentChar == '}') {
            updateLexeme();
            return new Token(closeBracket, lexeme, row);
        }
        else if (currentChar == ';') {
            updateLexeme();
            return new Token(semicolon, lexeme, row);
        }
        else if (currentChar == ',') {
            updateLexeme();
            return new Token(comma, lexeme, row);
        }
        else if (currentChar == '.') {
            updateLexeme();
            return new Token(dot, lexeme, row);
        }
        else if (currentChar == ':') {
            updateLexeme();
            return new Token(colon, lexeme, row);
        }
        else if (SourceManager.END_OF_FILE == currentChar){
            updateLexeme();
            return new Token(END_OF_FILE, "EOF", row);
        }
        else {
            updateLexeme();
            throw new LexicalException(LexicalErrorMessage.invalidSymbol(currentChar, sourceManager));
        }
    }

    private Token idClase() {
        if (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            updateCurrentChar();
            return idClase();
        }
        else {
            return new Token(idClase, lexeme, row);
        }
    }

    private Token idMetVar() {
        if (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            updateCurrentChar();
            return idMetVar();
        }
        else {
            TokenType tokenType = TokenType.fromExplanation(lexeme);
            if (tokenType != null) {
                return new Token(tokenType, lexeme, row);
            }
            else {
                return new Token(idMetVar, lexeme, row);
            }
        }
    }

    private Token intLiteral() throws LexicalException {
        if (Character.isDigit(currentChar)){
            if (lexeme.length() < MAX_DIGITS) {
                updateLexeme();
                updateCurrentChar();
                return intLiteral();
            }
            else {
                updateLexeme();
                throw new LexicalException(LexicalErrorMessage.integerTooLong(lexeme, sourceManager));
            }
        }
        else {
            return new Token(intLiteral, lexeme, row);
        }
    }

    private Token charLiteral() throws LexicalException {
        if (currentChar == '\'') {
            throw new LexicalException(LexicalErrorMessage.invalidCharacter(lexeme, sourceManager));
        }
        else if (currentChar == ENTER || currentChar == NEW_LINE || currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(LexicalErrorMessage.invalidCarriageReturn(lexeme, sourceManager));
        }
        else if (currentChar == '\\') {
            updateLexeme();
            updateCurrentChar();
            return specialChar();
        }
        else {
            updateLexeme();
            updateCurrentChar();
            return closeCharLiteral();
        }
    }

    private Token specialChar() throws LexicalException {
        if (currentChar == 'u') {
            updateLexeme();
            updateCurrentChar();
            return unicodeChar();
        }
        else {
            updateLexeme();
            updateCurrentChar();
            return closeCharLiteral();
        }
    }

    private Token unicodeChar() throws LexicalException {
        if (Character.isLetterOrDigit(currentChar)) {
            if (lexeme.length() <= MAX_UNICODE_DIGITS + 2) { 
                updateLexeme();
                updateCurrentChar();
                return unicodeChar();
            }
            else {
                throw new LexicalException(LexicalErrorMessage.invalidCharacter(lexeme, sourceManager));
            }
        }
        else if (currentChar == '\'' && lexeme.length() == MAX_UNICODE_DIGITS + 3) {
            updateLexeme();
            updateCurrentChar();
            return new Token(charLiteral, lexeme, row);
        }
        else {
            throw new LexicalException(LexicalErrorMessage.invalidCharacter(lexeme, sourceManager));
        }
    }

    private Token closeCharLiteral() throws LexicalException {
        if (currentChar == '\'') {
            updateLexeme();
            updateCurrentChar();
            return new Token(charLiteral, lexeme, row);
        }
        else {
            throw new LexicalException(LexicalErrorMessage.invalidCharacter(lexeme, sourceManager));
        }
    }

    private Token stringLiteral() throws LexicalException {
        if (currentChar == ENTER || currentChar == NEW_LINE || currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(LexicalErrorMessage.invalidCarriageReturn(lexeme, sourceManager));
        }
        else if (currentChar == '\\') {
            updateLexeme();
            updateCurrentChar();
            return specialString();
        }
        else if (currentChar == '"') {
            updateLexeme();
            updateCurrentChar();
            return closeStringLiteral();
        }
        else {
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        }
    }

    private Token specialString() throws LexicalException {
        if (currentChar == NEW_LINE || currentChar == ENTER || currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(LexicalErrorMessage.invalidCarriageReturn(lexeme, sourceManager));
        }
        else {
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        }
    }

    private Token closeStringLiteral() {
        return new Token(stringLiteral, lexeme, row);
    }

    private Token slashSymbol() throws LexicalException {
        if (currentChar == '*') {
            updateLexeme();
            updateCurrentChar();
            return multiLineComment();
        }
        else if (currentChar == '/') {
            updateLexeme();
            updateCurrentChar();
            return singleLineComment();
        }
        else {
            return new Token(divOp, lexeme, row);
        }
    }

    private Token multiLineComment() throws LexicalException {
        if (currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(LexicalErrorMessage.invalidEndOfFile(lexeme, sourceManager));
        }
        else if (currentChar == '*') {
            updateCurrentChar();
            return closeMultiLineComment();
        }
        else {
            updateCurrentChar();
            return multiLineComment();
        }
    }

    private Token closeMultiLineComment() throws LexicalException {
        if (currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(LexicalErrorMessage.invalidMultilineComment(lexeme, sourceManager));
        }
        else if (currentChar == '/') {
            return nextToken();
        }
        else if (currentChar == '*') {
            updateLexeme();
            updateCurrentChar();
            return closeMultiLineComment();
        }
        else {
            updateLexeme();
            updateCurrentChar();
            return multiLineComment();
        }
    }

    private Token singleLineComment() throws LexicalException {
        if (currentChar == SourceManager.END_OF_FILE) {
            return nextToken();
        }
        else if (currentChar == NEW_LINE) {
            return nextToken();
        }
        else {
            updateCurrentChar();
            return singleLineComment();
        }
    }

    private Token greaterSymbol() {
        if (currentChar == '=') {
            updateLexeme();
            updateCurrentChar();
            return new Token(greaterEqualOp, lexeme, row);
        }
        else {
            return new Token(greaterOp, lexeme, row);
        }
    }

    private Token lesserSymbol() {
        if (currentChar == '=') {
            updateLexeme();
            updateCurrentChar();
            return new Token(lesserEqualOp, lexeme, row);
        }
        else {
            return new Token(lesserOp, lexeme, row);
        }
    }

    private Token notSymbol() {
        if (currentChar == '=') {
            updateLexeme();
            updateCurrentChar();
            return new Token(notEqualOp, lexeme, row);
        }
        else {
            return new Token(notOp, lexeme, row);
        }
    }

    private Token equalSymbol() {
        if (currentChar == '=') {
            updateLexeme();
            updateCurrentChar();
            return new Token(equalsOp, lexeme, row);
        }
        else {
            return new Token(assignOp, lexeme, row);
        }
    }

    private Token plusSymbol() {
        if (currentChar == '+') {
            updateLexeme();
            updateCurrentChar();
            return new Token(incrementOp, lexeme, row);
        }
        else {
            return new Token(plusOp, lexeme, row);
        }
    }

    private Token minusSymbol() {
        if (currentChar == '-') {
            updateLexeme();
            updateCurrentChar();
            return new Token(decrementOp, lexeme, row);
        }
        else {
            return new Token(minusOp, lexeme, row);
        }
    }

    private Token ampersandSymbol() throws LexicalException {
        if (currentChar == '&') {
            updateLexeme();
            updateCurrentChar();
            return new Token(andOp, lexeme, row);
        }
        else {
            throw new LexicalException(LexicalErrorMessage.invalidAmpersandUsage(lexeme, sourceManager));
        }
    }

    private Token pipeSymbol() throws LexicalException {
        if (currentChar == '|') {
            updateLexeme();
            updateCurrentChar();
            return new Token(orOp, lexeme, row);
        }
        else {
            throw new LexicalException(LexicalErrorMessage.invalidPipeUsage(lexeme, sourceManager));
        }
    }
}