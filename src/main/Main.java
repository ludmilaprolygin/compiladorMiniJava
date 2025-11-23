package main;

import lexicalAnalyzer.LexicalAnalyzer;
import model.codeGeneration.Instructions;
import model.symbolTable.MainElement;
import model.symbolTable.SymbolTable;
import outputManager.OutputManager;
import sourceManager.*;
import model.Token;
import model.TokenType;
import syntacticAnalyzer.SyntacticAnalyzer;
import utils.exceptions.LexicalException;
import utils.exceptions.SemanticException;
import utils.messages.GenericErrorMessage;
import utils.messages.LexicalConsoleMessage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static SourceManager sourceManager;
    private static OutputManager outputManager;
    private static LexicalAnalyzer lexicalAnalyzer;
    private static SyntacticAnalyzer syntacticAnalyzer;
    private static final SymbolTable symbolTable = SymbolTable.symbolTable();

    public static void main(String[] args) {
        initialize();

        if (args.length == 1) {
            try {
                String fileName = args[0];
                String outputFileName = "[" + fileName + "].out";
                //lexicalAnalysis();
                openFile(fileName);
                loadPredefined();
                syntacticAnalysis(fileName);
                closeFile();
                semanticAnalysis();
                codeGeneration(outputFileName);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        else if (args.length == 2) {
            try {
                String fileName = args[0];
                String outputFileName = args[1];
                //lexicalAnalysis();
                loadPredefined();
                openFile(fileName);
                syntacticAnalysis(fileName);
                closeFile();
                semanticAnalysis();
                codeGeneration(outputFileName);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            System.out.println(GenericErrorMessage.MISUSE_ERROR);
        }
    }

    public static void initialize(){
        sourceManager = new SourceManagerImplEficiente();
        lexicalAnalyzer = new LexicalAnalyzer();
        symbolTable.reset();
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
            System.out.println(sourceManager.getFileName());
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
        while (token == null || token.getTokenType() != TokenType.END_OF_FILE);

        System.out.println(lexicalConsoleMessage.getSuccessMessage());
        System.out.println(lexicalConsoleMessage.getErrorMessage());
    }

    private static void syntacticAnalysis(String fileName) throws Exception {
        lexicalAnalyzer.init(sourceManager);
        syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer, true);
        closeFile();

        openFile(fileName);
        lexicalAnalyzer.init(sourceManager);
        syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer, false);
    }

    private static void loadPredefined() {
        try{
            String predefinedFilePath = "src/model/symbolTable/Predefined.txt";
            openFile(predefinedFilePath);
            syntacticAnalysis(predefinedFilePath);
            closeFile();
        }
        catch(Exception e){

        }
    }

    private static void semanticAnalysis() throws SemanticException{
        symbolTable.correctDeclaration();
        symbolTable.consolidate();
        //System.out.println(symbolTable);
        symbolTable.check();

        System.out.println("Compilación exitosa. \n[SinErrores]");
    }

    private static void codeGeneration(String outputFileName) throws Exception {
        outputManager = new OutputManager(outputFileName);

        genInit();
        outputManager.genHeap();

        symbolTable.gen(outputManager);

        outputManager.close();
    }

    private static void genInit() {
        model.symbolTable.Class mainClass = (model.symbolTable.Class) symbolTable.getMainClass();
        String mainLabel = "lbl_main@" + mainClass.getName().getLexeme();

        outputManager.gen(".CODE");
        outputManager.gen(Instructions.PUSH + " simple_heap_init");
        outputManager.gen(Instructions.CALL.toString());
        outputManager.gen(Instructions.PUSH + " " + mainLabel);
        outputManager.gen(Instructions.CALL.toString());
        outputManager.gen(Instructions.HALT.toString());
        outputManager.gen("");
        outputManager.gen("");
    }
}