package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import model.Firsts;
import model.Token;
import model.TokenType;
import utils.exceptions.SyntacticException;
import utils.messages.SyntacticErrorMessage;

import java.util.ArrayList;

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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, tokenType.toString()));
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
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Clase, currentTokenType)) {
            clase();
            listaClases();
        }
        else { /* epsilon */ }
    }

    private void clase() throws Exception {
        modificadorOpcional();
        match(reservedClass);
        match(idClase);
        _tipoParametricoOpcional();
        herenciaOpcional();
        _interfaceOpcional();
        match(openBracket);
        listaMiembros();
        match(closeBracket);
    }

    private void _tipoParametricoOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(firsts.containsToken(_TipoParametricoOpcional, currentTokenType)) {
            match(lesserOp);
            match(idClase);
            match(greaterOp);
        }
        else { /* epsilon */ }
    }

    private void modificadorOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(ModificadorOpcional, currentTokenType)) {
            match(currentTokenType);
        }
        else { /* epsilon */ }
    }

    private void herenciaOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(HerenciaOpcional, currentTokenType)) {
            match(reservedExtends);
            match(idClase);
            _tipoParametricoOpcional();
        }
        else { /* epsilon */ }
    }

    private void _interfaceOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_InterfaceOpcional, currentTokenType)) {
            match(reservedImplements);
            match(idClase);
            _tipoParametricoOpcional();
        }
        else { /* epsilon */ }
    }

    private void listaMiembros() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Miembro, currentTokenType)) {
            miembro();
            listaMiembros();
        } else { /* epsilon */ }
    }

    private void miembro() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Tipo, currentTokenType)) {
            tipo();
            _tipoParametricoOpcional();
            match(idMetVar);
            _restoMiembro();
        }
        else if (currentTokenType.equals(reservedVoid)) {
            match(reservedVoid);
            match(idMetVar);
            argsFormales();
            bloqueOpcional();
        }
        else if (firsts.containsToken(_Modificador, currentTokenType)) {
            _modificador();
            tipoMetodo();
            _tipoParametricoOpcional();
            match(idMetVar);
            argsFormales();
            bloqueOpcional();
        }
        else if (firsts.containsToken(Constructor, currentTokenType)) {
            constructor();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Miembro).toString(), Miembro));
        }
    }

    private void _restoMiembro() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(semicolon)) {
            match(semicolon);
        }
        else if (firsts.containsToken(_InicializacionAtributoOpcional, currentTokenType)) {
            _inicializacionAtributoOpcional();
            match(semicolon);
        }
        else if (firsts.containsToken(ArgsFormales, currentTokenType)) {
            argsFormales();
            bloqueOpcional();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_RestoMiembro).toString(), Miembro));
        }
    }

    private void _inicializacionAtributoOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_InicializacionAtributoOpcional, currentTokenType)) {
            match(currentTokenType);
            operando();
        }
        else { /* epsilon */ }
    }

    private void _modificador() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_Modificador, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_Modificador).toString(), Miembro));
        }
    }

    private void constructor() throws Exception {
        match(reservedPublic);
        match(idClase);
        argsFormales();
        bloque();
    }

    private void tipoMetodo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Tipo, currentTokenType)) {
            tipo();
        }
        else if (currentTokenType.equals(reservedVoid)) {
            match(reservedVoid);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(TipoMetodo).toString(), TipoMetodo));
        }
    }

    private void tipo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            tipoPrimitivo();
        }
        else if (currentTokenType.equals(idClase)) {
            match(idClase);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Tipo).toString(), Tipo));
        }
    }

    private void tipoPrimitivo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            match(currentTokenType);
        }
        else { /* epsilon */ }
    }

    private void argsFormales() throws Exception {
        match(openParenthesis);
        listaArgsFormalesOpcional();
        match(closeParenthesis);
    }

    private void listaArgsFormalesOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(ListaArgsFormales, currentTokenType)) {
            listaArgsFormales();
        }
        else { /* epsilon */ }
    }

    private void listaArgsFormales() throws Exception {
        argFormal();
        _restoListaArgsFormales();
    }

    private void _restoListaArgsFormales() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(comma)) {
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
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Bloque, currentTokenType)) {
            bloque();
        }
        else if (currentTokenType.equals(semicolon)) {
            match(semicolon);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(BloqueOpcional).toString(), BloqueOpcional));
        }
    }

    private void bloque() throws Exception {
        match(openBracket);
        listaSentencias();
        match(closeBracket);
    }

    private void listaSentencias() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Sentencia, currentTokenType)) {
            sentencia();
            listaSentencias();
        }
        else { /* epsilon */ }
    }

    private void sentencia() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(semicolon)) {
            match(semicolon);
        }
        else if (firsts.containsToken(Expresion, currentTokenType)) {
            expresion();
            match(semicolon);
        }
        else if (firsts.containsToken(_VarLocalClasica, currentTokenType)) {
            _varLocalClasica();
            match(semicolon);
        }
        else if (firsts.containsToken(VarLocal, currentTokenType)) {
            varLocal();
            match(semicolon);
        }
        else if (firsts.containsToken(Return, currentTokenType)) {
            returnStatement();
            match(semicolon);
        }
        else if (firsts.containsToken(Bloque, currentTokenType)) {
            bloque();
        }
        else if (firsts.containsToken(If, currentTokenType)) {
            ifStatement();
        }
        else if (firsts.containsToken(While, currentTokenType)) {
            whileStatement();
        }
        else if (firsts.containsToken(_InicioFor, currentTokenType)) {
            _inicioFor();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Sentencia).toString(), Sentencia));
        }
    }

    private void _tipoClase() throws Exception {
        match(idClase);
    }

    private void _restoTipoClasePrimario() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(dot)) {
            match(dot);
            match(idMetVar);
            argsActuales();
        }
        else if (firsts.containsToken(_TipoParametricoOpcional, currentTokenType)) {
            _tipoParametricoOpcional();
            _restoVarLocalClasica();
            _asignacionOpcional();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Primario).toString(), Primario));
        }
    }

    private void varLocal() throws Exception {
        match(reservedVar);
        match(idMetVar);
        match(assignOp);
        expresionCompuesta();
        _restoVarLocal();
    }

    private void _restoVarLocal() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(questionMark)) {
            match(questionMark);
            expresion();
            match(colon);
            expresion();
        }
        else { /* epsilon */ }
    }

    private void returnStatement() throws Exception {
        match(reservedReturn);
        expresionOpcional();
    }

    private void expresionOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Expresion, currentTokenType)) {
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
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(reservedElse)) {
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
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorAsignacion, currentTokenType)) {
            operadorAsignacion();
            expresionCompuesta();
        }
        else { /* epsilon */ }
    }

    private void operadorAsignacion() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(assignOp)) {
            match(assignOp);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(OperadorAsignacion).toString(), OperadorAsignacion));
        }
    }

    private void expresionCompuesta() throws Exception {
        expresionBasica();
        _restoExpresionCompuesta();
    }

    private void _restoExpresionCompuesta() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorBinario, currentTokenType)) {
            operadorBinario();
            expresionBasica();
            _restoExpresionCompuesta();
        }
        else { /* epsilon */ }
    }

    private void operadorBinario() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorBinario, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(OperadorBinario).toString(), OperadorBinario));
        }
    }

    private void expresionBasica() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorUnario, currentTokenType)) {
            operadorUnario();
            operando();
        }
        else if (firsts.containsToken(Operando, currentTokenType)) {
            operando();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(ExpresionBasica).toString(), ExpresionBasica));
        }
    }

    private void operadorUnario() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorUnario, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(OperadorUnario).toString(), OperadorUnario));
        }
    }

    private void operando() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Primitivo, currentTokenType)) {
            primitivo();
        }
        else if (firsts.containsToken(Referencia, currentTokenType)) {
            referencia();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Operando).toString(), Operando));
        }
    }

    private void primitivo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Primitivo, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Primitivo).toString(), Primitivo));
        }
    }

    private void referencia() throws Exception {
        primario();
        _restoReferencia();
    }

    private void _restoReferencia() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_Encadenado, currentTokenType)) {
            _encadenado();
            _restoReferencia();
        }
        else { /* epsilon */ }
    }

    private void _encadenado () throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(dot)) {
            match(dot);
            match(idMetVar);
            _restoEncadenado();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_Encadenado).toString(), Referencia));
        }
    }

    private void _restoEncadenado () throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(ArgsActuales, currentTokenType)) {
            argsActuales();
        }
        else { /* epsilon */ }
    }

    private void primario() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(reservedThis) || currentTokenType.equals(stringLiteral)) {
            match(currentTokenType);
        }
        else if (firsts.containsToken(LlamadaConstructor, currentTokenType)) {
            llamadaConstructor();
        }
        else if (currentTokenType.equals(idMetVar)) {
            match(idMetVar);
            _restoLlamadaMetodo();
        }
        else if (firsts.containsToken(LlamadaMetodoEstatico, currentTokenType)) {
            llamadaMetodoEstatico();
        }
        else if (firsts.containsToken(ExpresionParentizada, currentTokenType)) {
            expresionParentizada();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Primario).toString(), Primario));
        }
    }

    private void _restoLlamadaMetodo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(ArgsActuales, currentTokenType)) {
            argsActuales();
        }
        else { /* epsilon */ }
    }

    private void llamadaConstructor() throws Exception {
        match(reservedNew);
        match(idClase);
        _genericidad();
        argsActuales();
    }

    private void _genericidad() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(lesserOp)) {
            match(lesserOp);
            _inicioGenericidad();
        }
        else { /* epsilon */ }
    }

    private void _inicioGenericidad() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(greaterOp)) {
            match(greaterOp);
        }
        else if (currentTokenType.equals(idClase)) {
            match(idClase);
            match(greaterOp);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_InicioGenericidad).toString(), _InicioGenericidad));
        }
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
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(ListaExps, currentTokenType)) {
            listaExps();
        }
        else { /* epsilon */ }
    }

    private void listaExps() throws Exception {
        expresion();
        _restoListaExps();
    }

    private void _restoListaExps() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(comma)) {
            match(comma);
            listaExps();
        }
        else { /* epsilon */ }
    }

    // Opcional Fors! E2
    private void _inicioFor() throws Exception {
        match(reservedFor);
        match(openParenthesis);
        _restoFor();
    }

    private void _restoFor() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_ForIteradores, currentTokenType)) {
            tipo();
            match(idMetVar);
            match(colon);
            match(idMetVar);
            match(closeParenthesis);
            bloqueOpcional();
        }
    }

    // Opcional Variables Locales Clásicas E2
    private void _varLocalClasica() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(idClase)) {
            _tipoClase();
            _tipoParametricoOpcional();
            match(idMetVar);
            _restoVarLocalClasica();
            _asignacionOpcional();
        }
        else if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            tipoPrimitivo();
            _tipoParametricoOpcional();
            match(idMetVar);
            _restoVarLocalClasica();
            _asignacionOpcional();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(VarLocal).toString(),VarLocal));
        }

    }

    private void _restoVarLocalClasica() throws Exception{
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(comma)) {
            match(comma);
            match(idMetVar);
            _restoVarLocalClasica();
        }
        else { /* epsilon */ }
    }

    private void _asignacionOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorAsignacion, currentTokenType)) {
            operadorAsignacion();
            expresion();
        }
        else { /* epsilon */ }
    }

    private void _valor() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_Valor, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_Valor).toString(), _Valor));
        }
    }
}