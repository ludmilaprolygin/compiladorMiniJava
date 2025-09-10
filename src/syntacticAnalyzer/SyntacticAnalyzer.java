package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import model.Token;
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

    private void inicial() throws Exception {
        listaClases();
        match("END_OF_FILE");
    }

    private void listaClases() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (!currentTokenName.equals("END_OF_FILE")) {
            clase();
            listaClases();
        }
        else { /* epsilon */ }
    }

    private void clase() throws Exception {
        modificadorOpcional();
        match("reservedClass");
        match("idClase");
        herenciaOpcional();
        match("openBracket");
        listaMiembros();
        match("closeBracket");
    }

    private void modificadorOpcional() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedAbstract")) {
            match("reservedAbstract");
        } else if (currentTokenName.equals("reservedFinal")) {
            match("reservedFinal");
        } else if (currentTokenName.equals("reservedStatic")) {
            match("reservedStatic");
        } else { /* epsilon */ }
    }

    private void herenciaOpcional() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedExtends")) {
            match("reservedExtends");
            match("idClase");
        } else { /* epsilon */ }
    }

    private void listaMiembros() throws Exception {
        String currentTokenName = getCurrentTokenName();
        //TODO
    }

    private void miembro() throws Exception {
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

    private void atributo() throws Exception {
        tipo();
        match("idMetVar");
    }

    private void metodo() throws Exception {
        modificadorOpcional();
        tipoMetodo();
        match("idMetVar");
        argsFormales();
        bloqueOpcional();
    }

    private void constructor() throws Exception {
        match("reservedPublic");
        match("idClase");
        argsFormales();
        bloque();
    }

    private void tipoMetodo() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("void")) {
            match("reservedVoid");
        } else {
            tipo();
        }
    }

    private void tipo() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("idClase")) {
            match("idClase");
        } else {
            tipoPrimitivo();
        }
    }

    private void tipoPrimitivo() throws Exception {
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

    private void argsFormales() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("openParenthesis")) {
            match("openParenthesis");
            listaArgsFormalesOpcional();
            match("closeParenthesis");
        } else {
            throw new SyntacticException(currentToken, "(");
        }
    }

    private void listaArgsFormalesOpcional() throws Exception {
        //TODO
    }

    private void listaArgsFormales() throws Exception {
        //TODO
    }

    private void argFormal() throws Exception {
        tipo();
        match("idMetVar");
    }

    private void bloqueOpcional() throws Exception {
        //TODO
    }

    private void bloque() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("openBracket")) {
            match("openBracket");
            listaSentencias();
            match("closeBracket");
        } else {
            throw new SyntacticException(currentToken, "{");
        }
    }

    private void listaSentencias() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (!currentTokenName.equals("closeParenthesis")) {
            sentencia();
            listaSentencias();
        }
        else { /* epsilon */ }
    }

    private void sentencia() throws Exception {
        //TODO
    }

    private void asignacion() throws Exception {
        expresion();
    }

    private void llamada() throws Exception {
        expresion();
    }

    private void varLocal() throws Exception {
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

    private void returnStatement() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedReturn")) {
            match("reservedReturn");
            expresionOpcional();
        } else {
            throw new SyntacticException(currentToken, "return");
        }
    }

    private void expresionOpcional() throws Exception {
        expresion();
    }

    private void ifStatement() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedIf")) {
            match("reservedIf");
            match("openParenthesis");
            expresion();
            match("closeParenthesis");
            sentencia();
            _restoIfStatement();
        } else {
            throw new SyntacticException(currentToken, "if");
        }
    }

    private void _restoIfStatement() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedElse")) {
            match("reservedElse");
            sentencia();
        } else { /* epsilon */ }
    }

    private void whileStatement() throws Exception {
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

    private void expresion() throws Exception {
        expresion();
        _restoExpresion();
    }

    private void _restoExpresion() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("assignOp") || currentTokenName.equals("plusOp") ||
                currentTokenName.equals("minusOp")) {
            operadorAsignacion();
            expresionCompuesta();
        } else { /* epsilon */ }
    }

    private void operadorAsignacion() throws Exception {
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

    private void expresionCompuesta() throws Exception {
        //TODO
    }

    private void operadorBinario() throws Exception {
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

    private void expresionBasica() throws Exception {
        operando();
    }

    private void operadorUnario() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("plusOp")) {
            match("plusOp");
        } else if (currentTokenName.equals("incOp")) {
            match("incOp");
        } else if (currentTokenName.equals("minusOp")) {
            match("minusOp");
        } else if (currentTokenName.equals("decOp")) {
            match("decOp");
        } else if (currentTokenName.equals("notOp")) {
            match("notOp");
        } else {
            throw new SyntacticException(currentToken, "+, ++, -, --, !");
        }
    }

    private void operando() throws Exception {
        //TODO
    }

    private void primitivo() throws Exception {
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
            throw new SyntacticException(currentToken, "true, false, intLiteral, charLiteral, null");
        }
    }

    private void referencia() throws Exception {
        //TODO
    }

    private void primario() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedThis")) {
            match("reservedThis");
        } else if (currentTokenName.equals("stringLiteral")) {
            match("stringLiteral");
            //TODO - Factorizacion con accesoVar y llamadaMetodo
        } else if (currentTokenName.equals("idMetVar")) {
            accesoVar();
        } else if (currentTokenName.equals("new")) {
            llamadaConstructor();
        } else if (currentTokenName.equals("idMetVar")) {
            llamadaMetodo();
        } else if (currentTokenName.equals("idClase")) {
            llamadaMetodoEstatico();
        } else if (currentTokenName.equals("openParenthesis")) {
            expresionParentizada();
        } else {
            throw new SyntacticException(currentToken, "this, stringLiteral, idMetVar, new, idClase, (");
        }
    }

    private void accesoVar() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("idMetVar")) {
            match("idMetVar");
        } else {
            throw new SyntacticException(currentToken, "idMetVar");
        }
    }

    private void llamadaConstructor() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("reservedNew")) {
            match("reservedNew");
            match("idClase");
            argsActuales();
        } else {
            throw new SyntacticException(currentToken, "new");
        }
    }

    private void expresionParentizada() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("openParenthesis")) {
            match("openParenthesis");
            expresion();
            match("closeParenthesis");
        } else {
            throw new SyntacticException(currentToken, "(");
        }
    }

    private void llamadaMetodo() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("idMetVar")) {
            match("idMetVar");
            argsActuales();
        } else {
            throw new SyntacticException(currentToken, "idMetVar");
        }
    }

    private void llamadaMetodoEstatico() throws Exception {
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

    private void argsActuales() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("openParenthesis")) {
            match("openParenthesis");
            listaExpsOpcional();
            match("closeParenthesis");
        } else {
            throw new SyntacticException(currentToken, "(");
        }
    }

    private void listaExpsOpcional() throws Exception {
        //TODO
    }

    private void listaExps() throws Exception {
        expresion();
        _restoListaExps();
    }

    private void _restoListaExps() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("comma")) {
            match("comma");
            listaExps();
        } else { /* epsilon */ }
    }

    private void varEncadenada() throws Exception {
        String currentTokenName = getCurrentTokenName();
        if (currentTokenName.equals("dot")) {
            match("dot");
            match("idMetVar");
        } else {
            throw new SyntacticException(currentToken, ". idMetVar");
        }
    }

    private void metodoEncadenado() throws Exception {
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
