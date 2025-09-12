package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import model.Firsts;
import model.Token;
import model.TokenType;
import utils.exceptions.SyntacticException;

import static model.SyntacticMethod.*;
import static model.TokenType.*;

public class SyntacticAnalyzer {
    private Token currentToken;
    private final LexicalAnalyzer lexicalAnalyzer;
    private static final Firsts firsts = new Firsts();

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws Exception {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.currentToken = lexicalAnalyzer.nextToken();
        inicial();
    }

    public void match (TokenType tokenType) throws Exception {
        if (tokenType.equals(getCurrentTokenType())) {
            currentToken = lexicalAnalyzer.nextToken();
        } else {
            throw new SyntacticException(currentToken, tokenType.toString());
        }
    }

    private TokenType getCurrentTokenType() {
        return currentToken.getTokenType();
    }

    private void inicial() throws Exception {
        listaClases();
        match(END_OF_FILE);
    }

    private void listaClases() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Clase, currentToken)) {
            clase();
            listaClases();
        }
        else { /* epsilon */ }
    }

    private void clase() throws Exception {
        modificadorOpcional();
        match(reservedClass);
        match(idClase);
        herenciaOpcional();
        match(openBracket);
        listaMiembros();
        match(closeBracket);
    }

    private void modificadorOpcional() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(ModificadorOpcional, currentToken)) {
            match(currentToken);
        }
        else { /* epsilon */ }
    }

    private void herenciaOpcional() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(HerenciaOpcional, currentToken)) {
            match(reservedExtends);
            match(idClase);
        }
        else { /* epsilon */ }
    }

    private void listaMiembros() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Miembro, currentToken)) {
            miembro();
            listaMiembros();
        } else { /* epsilon */ }
    }

    private void miembro() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Tipo, currentToken)) {
            tipo();
            match(idMetVar);
            _restoMiembro();
        }
        else if (currentToken.equals(reservedVoid)) {
            match(reservedVoid);
            match(idMetVar);
            argsFormales();
            bloqueOpcional();
        }
        else if (firsts.containsToken(_Modificador, currentToken)) {
            _modificador();
            tipoMetodo();
            match(idMetVar);
            argsFormales();
            bloqueOpcional();
        }
        else if (firsts.containsToken(Constructor, currentToken)) {
            constructor();
        }
        else {
            //TODO - error
        }
    }

    private void _restoMiembro() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (currentToken.equals(semicolon)) {
            match(semicolon);
        }
        else if (firsts.containsToken(ArgsFormales, currentToken)) {
            argsFormales();
            bloqueOpcional();
        }
        else {
            //TODO - error
        }
    }

    private void _modificador() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(_Modificador, currentToken)) {
            match(currentToken);
        }
        else {
            //TODO - error
        }
    }

    private void constructor() throws Exception {
        match(reservedPublic);
        match(idClase);
        argsFormales();
        bloque();
    }

    private void tipoMetodo() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Tipo, currentToken)) {
            tipo();
        }
        else if (currentToken.equals(reservedVoid)) {
            match(reservedVoid);
        }
        else {
            //TODO - error
        }
    }

    private void tipo() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(TipoPrimitivo, currentToken)) {
            tipoPrimitivo();
        }
        else if (currentToken.equals(idClase)) {
            match(idClase);
        }
        else {
            //TODO - error
        }
    }

    private void tipoPrimitivo() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(TipoPrimitivo, currentToken)) {
            match(currentToken);
        }
        else { /* epsilon */ }
    }

    private void argsFormales() throws Exception {
        match(openParenthesis);
        listaArgsFormalesOpcional();
        match(closeParenthesis);
    }

    private void listaArgsFormalesOpcional() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(ListaArgsFormales, currentToken)) {
            listaArgsFormales();
        }
        else { /* epsilon */ }
    }

    private void listaArgsFormales() throws Exception {
        argFormal();
        _restoListaArgsFormales();
    }

    private void _restoListaArgsFormales() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (currentToken.equals(comma)) {
            match(comma);
            argFormal();
            _restoListaArgsFormales();
        }
        else { /* epsilon */ }
    }

    private void argFormal() throws Exception {
        tipo();
        match(idMetVar);
    }

    private void bloqueOpcional() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Bloque, currentToken)) {
            bloque();
        }
        else { /* epsilon */ }
    }

    private void bloque() throws Exception {
        match(openBracket);
        listaSentencias();
        match(closeBracket);
    }

    private void listaSentencias() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Sentencia, currentToken)) {
            sentencia();
            listaSentencias();
        }
        else { /* epsilon */ }
    }

    private void sentencia() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (currentToken.equals(semicolon)) {
            match(semicolon);
        }
        else if (firsts.containsToken(Expresion, currentToken)) {
            expresion();
            match(semicolon);
        }
        else if (firsts.containsToken(VarLocal, currentToken)) {
            varLocal();
            match(semicolon);
        }
        else if (firsts.containsToken(Return, currentToken)) {
            returnStatement();
            match(semicolon);
        }
        else if (firsts.containsToken(Bloque, currentToken)) {
            bloque();
        }
        else if (firsts.containsToken(If, currentToken)) {
            ifStatement();
        }
        else if (firsts.containsToken(While, currentToken)) {
            whileStatement();
        }
        else {
            //TODO - error
        }
    }

    private void varLocal() throws Exception {
        match(reservedVar);
        match(idMetVar);
        match(assignOp);
        expresionCompuesta();
    }

    private void returnStatement() throws Exception {
        match(reservedReturn);
        expresionOpcional();
    }

    private void expresionOpcional() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Expresion, currentToken)) {
            expresion();
        }
        else { /* epsilon */ }
    }

    private void ifStatement() throws Exception {
        match(reservedIf);
        match(openParenthesis);
        expresion();
        match(closeParenthesis);
        sentencia();
        _restoIfStatement();
    }

    private void _restoIfStatement() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (currentToken.equals(reservedElse)) {
            match(reservedElse);
            sentencia();
        }
        else { /* epsilon */ }
    }

    private void whileStatement() throws Exception {
        match(reservedWhile);
        match(openParenthesis);
        expresion();
        match(closeParenthesis);
        sentencia();
    }

    private void expresion() throws Exception {
        expresionCompuesta();
        _restoExpresion();
    }

    private void _restoExpresion() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(OperadorAsignacion, currentToken)) {
            operadorAsignacion();
            expresionCompuesta();
        }
        else { /* epsilon */ }
    }

    private void operadorAsignacion() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (currentToken.equals(assignOp)) {
            match(assignOp);
        }
        else if (currentToken.equals(plusOp)) {
            match(plusOp);
            match(assignOp);
        }
        else if (currentToken.equals(minusOp)) {
            match(minusOp);
            match(assignOp);
        }
        else {
            //TODO - error
        }
    }

    private void expresionCompuesta() throws Exception {
        expresionBasica();
        _restoExpresionCompuesta();
    }

    private void _restoExpresionCompuesta() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(OperadorBinario, currentToken)) {
            operadorBinario();
            expresionBasica();
            _restoExpresionCompuesta();
        }
        else { /* epsilon */ }
    }

    private void operadorBinario() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(OperadorBinario, currentToken)) {
            match(currentToken);
        }
        else {
            //TODO - error
        }
    }

    private void expresionBasica() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(OperadorUnario, currentToken)) {
            operadorUnario();
            operando();
        }
        else if (firsts.containsToken(Operando, currentToken)) {
            operando();
        }
        else {
            //TODO - error
        }
    }

    private void operadorUnario() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(OperadorUnario, currentToken)) {
            match(currentToken);
        }
        else {
            //TODO - error
        }
    }

    private void operando() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Primitivo, currentToken)) {
            primitivo();
        }
        else if (firsts.containsToken(Referencia, currentToken)) {
            referencia();
        }
    }

    private void primitivo() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(Primitivo, currentToken)) {
            match(currentToken);
        }
    }

    private void referencia() throws Exception {
        primario();
        _restoReferencia();
    }

    private void _restoReferencia() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(_Encadenado, currentToken)) {
            _encadenado();
            _restoReferencia();
        }
        else { /* epsilon */ }
    }

    private void _encadenado () throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (currentToken.equals(dot)) {
            match(dot);
            match(idMetVar);
            _restoEncadenado();
        }
        else {
            //TODO - error
        }
    }

    private void _restoEncadenado () throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(ArgsActuales, currentToken)) {
            argsActuales();
        }
        else { /* epsilon */ }
    }

    private void primario() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (currentToken.equals(reservedThis) || currentToken.equals(stringLiteral)) {
            match(currentToken);
        }
        else if (firsts.containsToken(LlamadaConstructor, currentToken)) {
            llamadaConstructor();
        }
        else if (currentToken.equals(idMetVar)) {
            match(idMetVar);
            _restoLlamadaMetodo();
        }
        else if (firsts.containsToken(LlamadaMetodoEstatico, currentToken)) {
            llamadaMetodoEstatico();
        }
        else if (firsts.containsToken(ExpresionParentizada, currentToken)) {
            expresionParentizada();
        }
        else {
            //TODO - error
        }
    }

    private void _restoLlamadaMetodo() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(ArgsActuales, currentToken)) {
            argsActuales();
        }
        else { /* epsilon */ }
    }

    private void llamadaConstructor() throws Exception {
        match(reservedNew);
        match(idClase);
        argsActuales();
    }
    private void llamadaMetodoEstatico() throws Exception {
        match(idClase);
        match(dot);
        match(idMetVar);
        argsActuales();
    }

    private void expresionParentizada() throws Exception {
        match(openParenthesis);
        expresion();
        match(closeParenthesis);
    }

    private void argsActuales() throws Exception {
        match(openParenthesis);
        listaExpsOpcional();
        match(closeParenthesis);
    }

    private void listaExpsOpcional() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (firsts.containsToken(ListaExps, currentToken)) {
            listaExps();
        }
        else { /* epsilon */ }
    }

    private void listaExps() throws Exception {
        expresion();
        _restoListaExps();
    }

    private void _restoListaExps() throws Exception {
        TokenType currentToken = getCurrentTokenType();
        if (currentToken.equals(comma)) {
            match(comma);
            listaExps();
        }
        else { /* epsilon */ }
    }
}