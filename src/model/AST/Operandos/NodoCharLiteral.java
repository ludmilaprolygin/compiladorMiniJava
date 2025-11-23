package model.AST.Operandos;

import model.AST.Encadenados.Encadenado;
import model.Token;
import model.codeGeneration.Instructions;
import model.symbolTable.AbstractType;
import model.symbolTable.CharType;
import outputManager.OutputManager;
import utils.exceptions.SemanticException;

public class NodoCharLiteral extends NodoOperando {
    public NodoCharLiteral(Token token){
        super(token);
    }

    @Override
    public AbstractType check() throws SemanticException {
        return new CharType(token);
    }

    @Override
    public void gen(OutputManager o) {
        int prepToken = (int) token.getLexeme().charAt(1); // Obtener el valor ASCII del carácter
        if (token.getLexeme().charAt(1) == '\\') { // Manejar caracteres de escape
            switch (token.getLexeme().charAt(2)) {
                case 'n':
                    prepToken = 10; // Nueva línea
                    break;
                case 't':
                    prepToken = 9; // Tabulación
                    break;
                case 'r':
                    prepToken = 13; // Retorno de carro
                    break;
                case '\'':
                    prepToken = 39; // Comilla simple
                    break;
                case '\"':
                    prepToken = 34; // Comilla doble
                    break;
                case '\\':
                    prepToken = 92; // Barra invertida
                    break;
                default:
                    prepToken = (int) token.getLexeme().charAt(2); // Otros caracteres de escape
                    break;
            }
        }
        o.gen(Instructions.PUSH + " " + prepToken);
    }
}


