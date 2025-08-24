package utils.messages;

import sourceManager.SourceManager;

public class LexicalErrorMessage {

    public static String invalidSymbol(char invalidChar, SourceManager sourceManager) {
        int row = sourceManager.getLineNumber();
        
        String message = "Error lexico en linea " + row + ": " + invalidChar + " no es un simbolo valido." + '\n';
        message = message + formatGeneralError(invalidChar, sourceManager);
        
        return message;
    }

    public static String integerTooLong(String lexeme, SourceManager sourceManager) {
        int row = sourceManager.getLineNumber();
        
        String message = "Error lexico en linea " + row + ": " + lexeme + " es un literal entero demasiado largo." + '\n';
        message = message + formatGeneralError(lexeme.charAt(0), sourceManager);
        
        //TODO: si el error es nro muy largo, deberia poner el digito que lo rompe o el lexema entero?

        return message;
    }

    public static String invalidCharacter(String lexeme, SourceManager sourceManager) {
        int row = sourceManager.getLineNumber();
        
        String message = "Error lexico en linea " + row + ": " + lexeme + " no es un literal char valido." + '\n';
        message = message + formatGeneralError(lexeme.charAt(0), sourceManager);
        
        return message;
    }

    public static String invalidUnicodeCharacter(String lexeme, SourceManager sourceManager) {
        int row = sourceManager.getLineNumber();
        
        String message = "Error lexico en linea " + row + ": " + lexeme + " no es un caracter unicode valido." + '\n';
        message = message + formatGeneralError(lexeme.charAt(0), sourceManager);
        
        return message;
    }

    public static String invalidStringLiteral(String lexeme, SourceManager sourceManager) {
        int row = sourceManager.getLineNumber();
        
        String message = "Error lexico en linea " + row + ": " + lexeme + " no es un string valido." + '\n';
        message = message + formatGeneralError(lexeme.charAt(0), sourceManager);
        
        return message;
    }

    public static String invalidCarriageReturn(String lexeme, SourceManager sourceManager) {
        int row = sourceManager.getLineNumber();
        
        String message = "Error lexico en linea " + row + ": " + lexeme + " no se encontro un cierre de cadena de caracter previo al retorno de carro." + '\n';
        message = message + formatGeneralError(lexeme.charAt(0), sourceManager);
        
        return message;
    }

    private static String formatGeneralError(char invalidChar, SourceManager sourceManager) {
        String message = "";
        int row = sourceManager.getLineNumber();
        int column = sourceManager.getColumnNumber();
        
        message = message + "Detalle: " + sourceManager.getCurrentLine() + '\n';
        for (int i = 0; i <= column; i++) {
            message = message + " ";
        }
        message = message + "^" + '\n';
        message = message + "[Error:" + invalidChar + "|" + row + "]";
        
        return message;
    }
}