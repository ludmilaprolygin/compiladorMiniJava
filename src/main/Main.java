package main;

import lexicalAnalyzer.LexicalAnalyzer;
import sourceManager.*;
import model.Token;
import model.TokenType;
import syntacticAnalyzer.SyntacticAnalyzer;
import utils.exceptions.LexicalException;
import utils.messages.GenericErrorMessage;
import utils.messages.LexicalConsoleMessage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static SourceManager sourceManager;
    private static LexicalAnalyzer lexicalAnalyzer;
    private static SyntacticAnalyzer syntacticAnalyzer;

    public static void main(String[] args) {
        initialize();

        if (args.length == 1) {
            String fileName = args[0];
            openFile(fileName);
            //lexicalAnalysis();
            syntacticAnalysis();
            closeFile();
        }
        else {
            System.out.println(GenericErrorMessage.MISUSE_ERROR);
        }
    }

    public static void initialize(){
        sourceManager = new SourceManagerImplEficiente();
        lexicalAnalyzer = new LexicalAnalyzer();
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
        lexicalAnalyzer.init(sourceManager);

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

    private static void syntacticAnalysis() {
        try {
            lexicalAnalyzer.init(sourceManager);
            syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);

            System.out.println("[SinErrores]");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}