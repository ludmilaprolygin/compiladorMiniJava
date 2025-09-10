package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import model.Token;
import utils.exceptions.LexicalException;
import utils.exceptions.SyntacticException;

public class SyntacticAnalyzer {
    private Token currentToken;
    private LexicalAnalyzer lexicalAnalyzer;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws Exception {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.currentToken = lexicalAnalyzer.nextToken();
        inicial();
    }

    public void match (String tokenName) throws Exception {
        if (tokenName.equals(getCurrentTokenName())) {
            currentToken = lexicalAnalyzer.nextToken();
        } else {
            throw new SyntacticException(currentToken, tokenName);
        }
    }

    private String getCurrentTokenName() {
        return currentToken.getTokenType().toString();
    }

    public void inicial() throws Exception {
        listaClases();
        match("END_OF_FILE");
    }

    public void listaClases() throws Exception {
        clase();
        // TODO - esto queda dando vueltas para siempre
        listaClases();
    }

    public void clase() throws Exception {
        modificadorOpcional();
        match("reservedClass");
        match("idClase");
        herenciaOpcional();
        match("openBracket");
        listaMiembros();
        match("closeBracket");
    }

    public void modificadorOpcional() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedAbstract")) {
            match("reservedAbstract");
        } else if (currentTokenName.equals("reservedFinal")) {
            match("reservedFinal");
        } else if (currentTokenName.equals("reservedStatic")) {
            match("reservedStatic");
        } else { /* epsilon */ }
    }

    public void herenciaOpcional() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedExtends")) {
            match("reservedExtends");
            match("idClase");
        } else { /* epsilon */ }
    }

    public void listaMiembros() throws Exception {
        String currentTokenName = getCurrentTokenName();

    }

    public void miembro() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("idClase") || currentTokenName.equals("reservedBoolean") ||
                currentTokenName.equals("reservedChar") || currentTokenName.equals("reservedInt")) {
            atributo();
        } else if (currentTokenName.equals("reservedAbstract") ||
                currentTokenName.equals("reservedFinal") ||
                currentTokenName.equals("reservedStatic")) {
            metodo();
        } else if (currentTokenName.equals("reservedPublic")) {
            constructor();
        } else {
            throw new SyntacticException(currentToken, "idClase, boolean, char, int, abstract, final, static, public");

        }
    }

    public void atributo() throws Exception {
        tipo();
        match("idMetVar");
    }

    public void metodo() throws Exception {
        modificadorOpcional();
        tipoMetodo();
        match("idMetVar");
        argsFormales();
        bloqueOpcional();
    }

    public void constructor() throws Exception {
        match("reservedPublic");
        match("idClase");
        argsFormales();
        bloque();
    }

    public void tipoMetodo() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("void")) {
            match("reservedVoid");
        } else {
            tipo();
        }
    }

    public void tipo() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("idClase")) {
            match("idClase");
        } else {
            tipoPrimitivo();
        }
    }

    public void tipoPrimitivo() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedBoolean")) {
            match("reservedBoolean");
        } else if (currentTokenName.equals("reservedChar")) {
            match("reservedChar");
        } else if (currentTokenName.equals("reservedInt")) {
            match("reservedInt");
        } else {
            throw new SyntacticException(currentToken, "boolean, char, int");
        }
    }

    public void argsFormales() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("openParenthesis")) {
            match("openParenthesis");
            listaArgsFormalesOpcional();
            match("closeParenthesis");
        } else {
            throw new SyntacticException(currentToken, "(");
        }
    }

    public void listaArgsFormalesOpcional() throws Exception {
        //TODO
    }

    public void listaArgsFormales() throws Exception {
        //TODO
    }

    public void argFormal() throws Exception {
        tipo();
        match("idMetVar");
    }

    public void bloqueOpcional() throws Exception {
        //TODO
    }

    public void bloque() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("openBracket")) {
            match("openBracket");
            listaSentencias();
            match("closeBracket");
        } else {
            throw new SyntacticException(currentToken, "{");
        }
    }

    public void listaSentencias() throws Exception {
        sentencia();
        listaSentencias();
    }

    public void sentencia() throws Exception {
        //TODO
    }

    public void asignacion() throws Exception {
        expresion();
    }

    public void llamada() throws Exception {
        expresion();
    }

    public void varLocal() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedVar")) {
            match("reservedVar");
            match("idMetVar");
            match("assignOp");
            expresionCompuesta();
        } else {
            throw new SyntacticException(currentToken, "var");
        }
    }

    public void returnSentencia() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedReturn")) {
            match("reservedReturn");
            expresionOpcional();
        } else {
            throw new SyntacticException(currentToken, "return");
        }
    }

    public void expresionOpcional() throws Exception {
        expresion();
    }

    public void ifSentencia() throws Exception {
        //TODO
    }

    public void whileSentencia() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedWhile")) {
            match("reservedWhile");
            match("openParenthesis");
            expresion();
            match("closeParenthesis");
            sentencia();
        } else {
            throw new SyntacticException(currentToken, "while");
        }
    }

    public void expresion() throws Exception {
        //TODO
    }

    public void operadorAsignacion() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("assignOp")) {
            match("assignOp");
        } else if (currentTokenName.equals("plusOp")) {
            match("plusOp");
            match("assignOp");
        } else if (currentTokenName.equals("minusOp")) {
            match("minusOp");
            match("assignOp");
        } else {
            throw new SyntacticException(currentToken, "=, +=, -=");
        }
    }

    public void expresionCompuesta() throws Exception {
        //TODO
    }

    public void operadorBinario() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("orOp")) {
            match("orOp");
        } else if (currentTokenName.equals("andOp")) {
            match("andOp");
        } else if (currentTokenName.equals("equalsOp")) {
            match("equalsOp");
        } else if (currentTokenName.equals("notEqualOp")) {
            match("notEqualOp");
        } else if (currentTokenName.equals("lesserOp")) {
            match("lesserOp");
        } else if (currentTokenName.equals("greaterOp")) {
            match("greaterOp");
        } else if (currentTokenName.equals("lesserEqualOp")) {
            match("lesserEqualOp");
        } else if (currentTokenName.equals("greaterEqualOp")) {
            match("greaterEqualOp");
        } else if (currentTokenName.equals("plusOp")) {
            match("plusOp");
        } else if (currentTokenName.equals("minusOp")) {
            match("minusOp");
        } else if (currentTokenName.equals("multOp")) {
            match("multOp");
        } else if (currentTokenName.equals("divOp")) {
            match("divOp");
        } else if (currentTokenName.equals("modOp")) {
            match("modOp");
        } else {
            throw new SyntacticException(currentToken, "||, &&, ==, !=, <, >, <=, >=, +, -, *, /, %");
        }
    }

    public void expresionBasica() throws Exception {
        operando();
    }

    public void operadorUnario() throws Exception {
        //TODO
    }

    public void operando() throws Exception {
        //TODO
    }

    public void primitivo() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("boolTrue")) {
            match("boolTrue");
        } else if (currentTokenName.equals("boolFalse")) {
            match("boolFalse");
        } else if (currentTokenName.equals("intLiteral")) {
            match("intLiteral");
        } else if (currentTokenName.equals("charLiteral")) {
            match("charLiteral");
        } else if (currentTokenName.equals("reservedNull")) {
            match("reservedNull");
        } else {
            throw new SyntacticException(currentToken, "true, false, intConst, charConst");
        }
    }

    public void referencia() throws Exception {
        //TODO
    }

    public void primario() throws Exception {
        //TODO
    }

    public void accesoVar() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("idMetVar")) {
            match("idMetVar");
        } else {
            throw new SyntacticException(currentToken, "idMetVar");
        }
    }

    public void llamadaConstructor() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedNew")) {
            match("reservedNew");
            match("idClase");
            argsActuales();
        } else {
            throw new SyntacticException(currentToken, "new");
        }
    }

    public void expresionParentizada() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("openParenthesis")) {
            match("openParenthesis");
            expresion();
            match("closeParenthesis");
        } else {
            throw new SyntacticException(currentToken, "(");
        }
    }

    public void llamadaMetodo() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("idMetVar")) {
            match("idMetVar");
            argsActuales();
        } else {
            throw new SyntacticException(currentToken, "idMetVar");
        }
    }

    public void llamadaMetodoEstatico() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("idClase")) {
            match("idClase");
            match("dot");
            match("idMetVar");
            argsActuales();
        } else {
            throw new SyntacticException(currentToken, "idClase");
        }
    }

    public void argsActuales() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("openParenthesis")) {
            match("openParenthesis");
            listaExpsOpcional();
            match("closeParenthesis");
        } else {
            throw new SyntacticException(currentToken, "(");
        }
    }

    public void listaExpsOpcional() throws Exception {
        //TODO
    }

    public void listaExps() throws Exception {
        //TODO
    }

    public void varEncadenada() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("dot")) {
            match("dot");
            match("idMetVar");
        } else {
            throw new SyntacticException(currentToken, ". idMetVar");
        }
    }

    public void metodoEncadenado() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("dot")) {
            match("dot");
            match("idMetVar");
            argsActuales();
        } else {
            throw new SyntacticException(currentToken, ". idMetVar");
        }
    }
}
