package utils.messages;

import model.SyntacticMethod;
import model.Token;
import syntacticAnalyzer.SyntacticAnalyzer;

public class SyntacticErrorMessage {
    public static String basicError(Token found, String expected, SyntacticMethod method) {
        String message = "Error sintáctico en linea " + found.getRow() +  ": se esperaba " + expected + " (por ser " + method + ") y se encontró " + found.getLexeme() + ". \n";
        message += "[Error:" + found.getLexeme() + "|" + found.getRow() + "]" + '\n';

        return message;

    }

    public static String basicError(Token found, String expected) {
        String message = "Error sintáctico en linea " + found.getRow() +  ": se esperaba " + expected + ") y se encontró " + found.getLexeme() + ". \n";
        message += "[Error:" + found.getLexeme() + "|" + found.getRow() + "]" + '\n';

        return message;

    }
}
