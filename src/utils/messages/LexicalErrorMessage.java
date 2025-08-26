package utils.messages;

import sourceManager.SourceManager;

public class LexicalErrorMessage {

    public static String invalidSymbol(char invalidChar, SourceManager sourceManager) {
        String message = basicError(sourceManager) + " " + invalidChar + " no es un simbolo valido." + '\n';
        message = message + errorEleganteFormatting(invalidChar, sourceManager);
        
        return message;
    }

    public static String integerTooLong(String lexeme, SourceManager sourceManager) {
        String message = basicError(sourceManager) + " " + lexeme + " es un literal entero demasiado largo." + '\n';
        message = message + errorEleganteFormatting(lexeme, sourceManager);

        return message;
    }

    public static String invalidCharacter(String lexeme, SourceManager sourceManager) {
        String message = basicError(sourceManager) + " " + lexeme + " no es un literal char valido." + '\n';
        message = message + errorEleganteFormatting(lexeme, sourceManager);
        
        return message;
    }

    public static String invalidUnicodeCharacter(String lexeme, SourceManager sourceManager) {
        String message = basicError(sourceManager) + " " + lexeme + " no es un caracter unicode valido." + '\n';
        message = message + errorEleganteFormatting(lexeme.charAt(0), sourceManager);
        
        return message;
    }

    public static String invalidMultilineComment(String lexeme, SourceManager sourceManager) {
        String message = basicError(sourceManager) + "no se encontro el cierre de comentario multilinea." + '\n';
        message = message + errorEleganteFormatting(lexeme, sourceManager);
        
        return message;
    }

    public static String invalidCarriageReturn(String lexeme, SourceManager sourceManager) {
        String message = basicError(sourceManager) + "no se encontro un cierre de cadena de caracteres previo al retorno de carro." + '\n';
        message = message + errorEleganteFormatting(lexeme, sourceManager);
        
        return message;
    }

    public static String invalidEndOfFile(String lexeme, SourceManager sourceManager) {
        String message = basicError(sourceManager) + "no se encontro un cierre de cadena de caracteres previo al final del archivo." + '\n';
        message = message + errorEleganteFormatting(lexeme, sourceManager);

        return message;
    }

    public static String invalidAmpersandUsage(String lexeme, SourceManager sourceManager) {
        String message = basicError(sourceManager) + "uso incorrecto del caracter & - considerar &&." + '\n';
        message = message + errorEleganteFormatting(lexeme, sourceManager);

        return message;
    }

    public static String invalidPipeUsage(String lexeme, SourceManager sourceManager) {
        String message = basicError(sourceManager) + "uso incorrecto del caracter | - considerar ||." + '\n';
        message = message + errorEleganteFormatting(lexeme, sourceManager);

        return message;
    }

    private static String basicError(SourceManager sourceManager) {
        int row = sourceManager.getLineNumber();
        int column = sourceManager.getColumnNumber();

        return "Error lexico en linea " + row + ", columna " + column + ": ";
    }


    private static String errorEleganteFormatting(char invalidChar, SourceManager sourceManager) {
         return errorEleganteFormatting(""+invalidChar, sourceManager);
    }

    private static String errorEleganteFormatting(String invalidLexeme, SourceManager sourceManager) {
        String message = "Detalle: ";
        int lineHeaderSize = message.length();
        int row = sourceManager.getLineNumber();
        int column = sourceManager.getColumnNumber();

        message = message + sourceManager.getCurrentLine() + '\n';

        for (int i = 0; i <= column + lineHeaderSize - 2; i++) {
            message = message + " ";
        }
        message = message + "^" + '\n';
        message = message + "[Error:" + invalidLexeme + "|" + row + "]" + '\n';

        return message;
    }
}