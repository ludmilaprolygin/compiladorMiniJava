package main;

import lexicalAnalyzer.LexicalAnalyzer;
import sourceManager.*;
import utils.format.Token;
import utils.format.TokenType;
import utils.exceptions.LexicalException;
import utils.messages.GenericErrorMessage;
import utils.messages.LexicalConsoleMessage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static SourceManager sourceManager;
    private static LexicalAnalyzer lexicalAnalyzer;

    public static void main(String[] args) {
        initialize();

        //TODO: ES ESTRICTAMENTE 1!!!!!!!!!!!!!!!!!!!!!1
        if (args.length <= 1) {
            String fileName = args[0];

            //TODO: BORRAR ESTO QUE SIGUE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //fileName = "resources/conErrores/lexConErrores01.java";

            openFile(fileName);
            //TODO manipular archivo
            lexicalAnalysis();
            closeFile();
        }
        else {
            System.out.println(GenericErrorMessage.MISUSE_ERROR);
        }
    }

    private static void initialize() {
        sourceManager = new SourceManagerImpl();
        lexicalAnalyzer = new LexicalAnalyzer(sourceManager);
    }

    private static void openFile (String fileName) {
        try {
            sourceManager.open(fileName);
        }
        catch (FileNotFoundException e) {
            System.out.println(GenericErrorMessage.FILE_NOT_FOUND_ERROR);
        }
    }

    private static void closeFile() {
        try {
            sourceManager.close();
        }
        catch (IOException e) {
            System.out.println(GenericErrorMessage.FILE_READ_ERROR);
        }
    }

    private static void lexicalAnalysis() {
        Token token = null;
        LexicalConsoleMessage lexicalConsoleMessage = new LexicalConsoleMessage();
        do  {
            try {
                token = lexicalAnalyzer.nextToken();
                lexicalConsoleMessage.appendSuccessMessage(token.format());
            }
            catch (LexicalException e) {
                lexicalConsoleMessage.appendErrorMessage(e.getMessage());
            }
        }
        while (token != null && token.getTokenType() != TokenType.END_OF_FILE);

        System.out.println(lexicalConsoleMessage.getSuccessMessage());
        System.out.println(lexicalConsoleMessage.getErrorMessage());
    }
}