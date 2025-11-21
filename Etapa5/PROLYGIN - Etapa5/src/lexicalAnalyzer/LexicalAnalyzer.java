package lexicalAnalyzer;

import java.io.IOException;

import sourceManager.SourceManager;

import model.Token;
import model.TokenType;
import utils.messages.LexicalErrorMessage;
import utils.messages.GenericErrorMessage;

import utils.exceptions.LexicalException;

import static model.TokenType.*;

public class LexicalAnalyzer {
    private static final int MAX_DIGITS = 9; 
    private static final int MAX_UNICODE_DIGITS = 4;
    private static final char ENTER = '\r';
    private static final char NEW_LINE = '\n';
 
    
    private SourceManager sourceManager;
    private char currentChar;
    private int row;
    private String lexeme;
    
    public LexicalAnalyzer() {
        lexeme = "";
    }

    public void init(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
        updateCurrentChar();
    }

    public Token nextToken() throws LexicalException {
        lexeme = "";
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
        Token toReturn;
        if (Character.isWhitespace(currentChar)) {
            updateCurrentChar();
            toReturn = e0();
        }
        else if (Character.isLetter(currentChar)) {
            if (Character.isUpperCase(currentChar)) {
                updateLexemeAndCurrentChar();
                toReturn = idClase();
            }
            else {
                updateLexemeAndCurrentChar();
                toReturn = idMetVar();
            }
        }
        else if (Character.isDigit(currentChar)) {
            updateLexemeAndCurrentChar();
            toReturn = intLiteral();
        }
        else if (currentChar == '\'') {
            updateLexemeAndCurrentChar();
            toReturn = charLiteral();
        }
        else if (currentChar == '"') {
            updateLexemeAndCurrentChar();
            toReturn = stringLiteral();
        }
        else if (currentChar == '/') {
            updateLexemeAndCurrentChar();
            toReturn = slashSymbol();
        }
        else if (currentChar == '>') {
            updateLexemeAndCurrentChar();
            toReturn = greaterSymbol();
        }
        else if (currentChar == '<') {
            updateLexemeAndCurrentChar();
            toReturn = lesserSymbol();
        }
        else if (currentChar == '!') {
            updateLexemeAndCurrentChar();
            toReturn = notSymbol();
        }
        else if (currentChar == '=') {
            updateLexemeAndCurrentChar();
            toReturn = equalSymbol();
        }
        else if (currentChar == '&') {
            updateLexemeAndCurrentChar();
            toReturn = ampersandSymbol();
        }
        else if (currentChar == '|') {
            updateLexemeAndCurrentChar();
            toReturn = pipeSymbol();
        }
        else if (currentChar == '%') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(modOp, lexeme, row);
        }
        else if (currentChar == '+') {
            updateLexemeAndCurrentChar();
            toReturn = plusSymbol();
        }
        else if (currentChar == '-') {
            updateLexemeAndCurrentChar();
            toReturn = minusSymbol();
        }
        else if (currentChar == '*') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(multOp, lexeme, row);
        }
        else if (currentChar == '(') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(openParenthesis, lexeme, row);
        }
        else if (currentChar == ')') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(closeParenthesis, lexeme, row);
        }
        else if (currentChar == '{') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(openBracket, lexeme, row);
        }
        else if (currentChar == '}') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(closeBracket, lexeme, row);
        }
        else if (currentChar == ';') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(semicolon, lexeme, row);
        }
        else if (currentChar == ',') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(comma, lexeme, row);
        }
        else if (currentChar == '.') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(dot, lexeme, row);
        }
        else if (currentChar == ':') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(colon, lexeme, row);
        }
        else if (currentChar == '?') {
            updateLexemeAndCurrentChar();
            toReturn = new Token(questionMark, lexeme, row);
        }
        else if (SourceManager.END_OF_FILE == currentChar){
            updateLexemeAndCurrentChar();
            toReturn = new Token(END_OF_FILE, "EOF", row);
        }
        else {
            char invalidChar = currentChar;
            updateLexemeAndCurrentChar();
            throw new LexicalException(LexicalErrorMessage.invalidSymbol(invalidChar, sourceManager));
        }
        return toReturn;
    }

    private Token idClase() {
        if (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            updateLexemeAndCurrentChar();
            return idClase();
        }
        else {
            return new Token(idClase, lexeme, row);
        }
    }

    private Token idMetVar() throws LexicalException {
        if (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            updateLexemeAndCurrentChar();
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
                updateLexemeAndCurrentChar();
                return intLiteral();
            }
            else {
                do {
                    updateLexemeAndCurrentChar();
                } while(Character.isDigit(currentChar));
                throw new LexicalException(LexicalErrorMessage.integerTooLong(lexeme, sourceManager));
            }
        }
        else {
            return new Token(intLiteral, lexeme, row);
        }
    }

    private Token charLiteral() throws LexicalException {
        if (currentChar == '\'' || currentChar == ENTER || currentChar == NEW_LINE || currentChar == SourceManager.END_OF_FILE) {
            updateLexeme();
            throw new LexicalException(LexicalErrorMessage.invalidCharacter(lexeme, sourceManager));
        }
        else if (currentChar == '\\') {
            updateLexemeAndCurrentChar();
            return specialChar();
        }
        else {
            updateLexemeAndCurrentChar();
            return closeCharLiteral();
        }
    }

    private Token specialChar() throws LexicalException {
        if (currentChar == 'u') {
            updateLexemeAndCurrentChar();
            return unicodeChar();
        }
        else {
            updateLexemeAndCurrentChar();
            return closeCharLiteral();
        }
    }

    private Token unicodeChar() throws LexicalException {
        if (Character.isLetterOrDigit(currentChar)) {
            if (lexeme.length() <= MAX_UNICODE_DIGITS + 2) {
                updateLexemeAndCurrentChar();
                return unicodeChar();
            }
            else {
                updateLexeme();
                throw new LexicalException(LexicalErrorMessage.invalidCharacter(lexeme, sourceManager));
            }
        }
        else if (currentChar == '\'' && lexeme.length() == MAX_UNICODE_DIGITS + 3) {
            updateLexemeAndCurrentChar();
            //String unicodeChar = (char) Integer.parseInt(lexeme.substring(3,7), 16) + "";
            return new Token(charLiteral, lexeme, row);
        }
        else {
            updateLexeme();
            throw new LexicalException(LexicalErrorMessage.invalidCharacter(lexeme, sourceManager));
        }
    }

    private Token closeCharLiteral() throws LexicalException {
        if (currentChar == '\'') {
            updateLexemeAndCurrentChar();
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
            updateLexemeAndCurrentChar();
            return specialString();
        }
        else if (currentChar == '"') {
            updateLexemeAndCurrentChar();
            return closeStringLiteral();
        }
        else {
            updateLexemeAndCurrentChar();
            return stringLiteral();
        }
    }

    private Token specialString() throws LexicalException {
        if (currentChar == NEW_LINE || currentChar == ENTER || currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(LexicalErrorMessage.invalidCarriageReturn(lexeme, sourceManager));
        }
        else {
            updateLexemeAndCurrentChar();
            return stringLiteral();
        }
    }

    private Token closeStringLiteral() {
        return new Token(stringLiteral, lexeme, row);
    }

    private Token slashSymbol() throws LexicalException {
        if (currentChar == '*') {
            updateLexemeAndCurrentChar();
            return multiLineComment(row);
        }
        else if (currentChar == '/') {
            updateLexemeAndCurrentChar();
            return singleLineComment();
        }
        else {
            return new Token(divOp, lexeme, row);
        }
    }

    private Token multiLineComment(int row) throws LexicalException {
        if (currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(LexicalErrorMessage.invalidMultilineComment("/*", row, sourceManager));
        }
        else if (currentChar == '*') {
            updateCurrentChar();
            return closeMultiLineComment(row);
        }
        else {
            updateCurrentChar();
            return multiLineComment(row);
        }
    }

    private Token closeMultiLineComment(int row) throws LexicalException {
        if (currentChar == SourceManager.END_OF_FILE) {
            throw new LexicalException(LexicalErrorMessage.invalidMultilineComment("/*", row, sourceManager));
        }
        else if (currentChar == '/') {
            return nextToken();
        }
        else if (currentChar == '*') {
            updateLexemeAndCurrentChar();
            return closeMultiLineComment(row);
        }
        else {
            updateLexemeAndCurrentChar();
            return multiLineComment(row);
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
            updateLexemeAndCurrentChar();
            return new Token(greaterEqualOp, lexeme, row);
        }
        else {
            return new Token(greaterOp, lexeme, row);
        }
    }

    private Token lesserSymbol() {
        if (currentChar == '=') {
            updateLexemeAndCurrentChar();
            return new Token(lesserEqualOp, lexeme, row);
        }
        else {
            return new Token(lesserOp, lexeme, row);
        }
    }

    private Token notSymbol() {
        if (currentChar == '=') {
            updateLexemeAndCurrentChar();
            return new Token(notEqualOp, lexeme, row);
        }
        else {
            return new Token(notOp, lexeme, row);
        }
    }

    private Token equalSymbol() {
        if (currentChar == '=') {
            updateLexemeAndCurrentChar();
            return new Token(equalsOp, lexeme, row);
        }
        else {
            return new Token(assignOp, lexeme, row);
        }
    }

    private Token plusSymbol() {
        if (currentChar == '+') {
            updateLexemeAndCurrentChar();
            return new Token(incrementOp, lexeme, row);
        }
        else {
            return new Token(plusOp, lexeme, row);
        }
    }

    private Token minusSymbol() {
        if (currentChar == '-') {
            updateLexemeAndCurrentChar();
            return new Token(decrementOp, lexeme, row);
        }
        else {
            return new Token(minusOp, lexeme, row);
        }
    }

    private Token ampersandSymbol() throws LexicalException {
        if (currentChar == '&') {
            updateLexemeAndCurrentChar();
            return new Token(andOp, lexeme, row);
        }
        else {
            throw new LexicalException(LexicalErrorMessage.invalidAmpersandUsage(lexeme, sourceManager));
        }
    }

    private Token pipeSymbol() throws LexicalException {
        if (currentChar == '|') {
            updateLexemeAndCurrentChar();
            return new Token(orOp, lexeme, row);
        }
        else {
            throw new LexicalException(LexicalErrorMessage.invalidPipeUsage(lexeme, sourceManager));
        }
    }

    private void updateLexemeAndCurrentChar() {
        updateLexeme();
        updateCurrentChar();
    }
}