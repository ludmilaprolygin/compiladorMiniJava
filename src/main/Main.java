package main;

import lexicalAnalyzer.LexicalAnalyzer;
import sourceManager.*;
import utils.messages.GenericErrorMessage;

public class Main {
    protected static SourceManager sourceManager;
    protected static LexicalAnalyzer lexicalAnalyzer;

    public static void main(String[] args) {
        initialize();

        if (args.length == 1) {
            //TODO manipular archivo
        }
        else {
            System.out.println(GenericErrorMessage.MISUSE_ERROR);
        }
    }

    protected static void initialize() {
        sourceManager = new SourceManagerImpl();
        lexicalAnalyzer = new LexicalAnalyzer(sourceManager);
    }
}