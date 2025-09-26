package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import model.Firsts;
import model.Following;
import model.Token;
import model.TokenType;
import model.symbolTable.SymbolTable;
import utils.exceptions.SyntacticException;
import utils.messages.SyntacticErrorMessage;

import static model.SyntacticMethod.*;
import static model.TokenType.*;
import model.symbolTable.Class;

public class SyntacticAnalyzer {
    private Token currentToken;
    private final LexicalAnalyzer lexicalAnalyzer;
    private static final Firsts firsts = new Firsts();
    private static final Following following = new Following(firsts);
    private final SymbolTable symbolTable = SymbolTable.symbolTable();

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
            Class c = clase();
            symbolTable.addClass(c.getName(), c);
            listaClases();
        }
        else if (firsts.containsToken(_Interface, currentTokenType)) {
            _interface();
            listaClases();
        }
        else { /* epsilon */ }
    }

    private void _interface() throws Exception {
        modificadorOpcional();
        match(reservedInterface);
        match(idClase);
        _tipoParametricoOpcional();
        _optionalParent();
        match(openBracket);
        _comportamientoInterface();
        match(closeBracket);
    }

    private void _comportamientoInterface() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_ComportamientoInterface, currentTokenType)) {
            _signaturaMetodo();
            _comportamientoInterface();
        }
        else { /* epsilon */ }
    }

    private void _signaturaMetodo() throws Exception{
        _visibilidadOpcional();
        tipoMetodo();
        _tipoParametricoOpcional();
        match(idMetVar);
        argsFormales();
        match(semicolon);
    }

    private Class clase() throws Exception {
        Class toReturn;
        Token modifier, name, parent;

        modifier = modificadorOpcional();
        match(reservedClass);
        name = currentToken;
        match(idClase);
        _tipoParametricoOpcional();
        parent = _optionalParent();

        toReturn = new Class(modifier, name, parent);
        symbolTable.setCurrentClass(toReturn);

        match(openBracket);
        listaMiembros();
        match(closeBracket);

        return toReturn;
    }

    private Token _optionalParent() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn = null;
        if (firsts.containsToken(HerenciaOpcional, currentTokenType)) {
            toReturn = herenciaOpcional();
        }
        else if (firsts.containsToken(_InterfaceOpcional, currentTokenType)) {
            toReturn = _interfaceOpcional();
        }
        else { /* epsilon */ }
        return toReturn;
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

    private Token modificadorOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn = null;
        if (firsts.containsToken(ModificadorOpcional, currentTokenType)) {
            toReturn = currentToken;
            match(currentTokenType);
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private Token herenciaOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn = null;
        if (firsts.containsToken(HerenciaOpcional, currentTokenType)) {
            match(reservedExtends);
            toReturn = currentToken;
            match(idClase);
            _tipoParametricoOpcional();
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private Token _interfaceOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn = null;
        if (firsts.containsToken(_InterfaceOpcional, currentTokenType)) {
            match(reservedImplements);
            toReturn = currentToken;
            match(idClase);
            _tipoParametricoOpcional();
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private void listaMiembros() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Miembro, currentTokenType)) {
            _miembroCompleto();
            listaMiembros();
        } else { /* epsilon */ }
    }

    private void _miembroCompleto() throws Exception {
        _visibilidadOpcional();
        miembro();
    }

    private void _visibilidadOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_Visibilidad, currentTokenType)) {
            match(currentTokenType);
        }
        else { /* epsilon */ }
    }

    private void miembro() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Tipo, currentTokenType)) {
            _decisorMiembroTipo();
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
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Miembro).toString()));
        }
    }

    private void _decisorMiembroTipo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(idClase)) {
            match(idClase);
            _decisorMiembroIdClase();
        }
        else if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            tipoPrimitivo();
            _tipoParametricoOpcional();
            match(idMetVar);
            _restoMiembro();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorMiembroTipo).toString()));
        }
    }

    private void _decisorMiembroIdClase() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(firsts.containsToken(ArgsFormales, currentTokenType)) {
            argsFormales();
            bloque();
        }
        else if (firsts.containsToken(_TipoParametricoOpcional, currentTokenType)) {
            _tipoParametricoOpcional();
            match(idMetVar);
            _restoMiembro();
        }
        else if (currentTokenType.equals(idMetVar)) {
            match(idMetVar);
            _restoMiembro();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorMiembroIdClase).toString()));
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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Miembro).toString()));
        }
    }

    private void _inicializacionAtributoOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_InicializacionAtributoOpcional, currentTokenType)) {
            match(currentTokenType);
            expresionCompuesta();
            _operadorTernario();
        }
        else { /* epsilon */ }
    }

    private void _modificador() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_Modificador, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_Modificador).toString()));
        }
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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(TipoMetodo).toString()));
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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Tipo).toString()));
        }
    }

    private void tipoPrimitivo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(TipoPrimitivo).toString()));
        }
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
        _tipoParametricoOpcional();
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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(BloqueOpcional).toString()));
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
        else if (currentTokenType.equals(idClase)) {
            match(idClase);
            _decisorExpresionIdClase();
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
        else if (firsts.containsToken(_ForStatement, currentTokenType)) {
            _forStatement();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Sentencia).toString()));
        }
    }

    private void _decisorExpresionIdClase() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(dot)) {
            match(dot);
            match(idMetVar);
            argsActuales();
        }
        else if (currentTokenType.equals(comma)) {
            _restoVarLocalClasica();
        }
        else if (firsts.containsToken(ExpresionCompuesta, currentTokenType)) {
            expresionCompuesta();
            _restoVarLocalClasica();
            _restoExpresion();
            _operadorTernario();
            match(semicolon);
        }
        else if (currentTokenType.equals(lesserOp)) {
            _tipoParametricoOpcional();
            match(idMetVar);
            _restoVarLocalClasica();
            _asignacionOpcional();

        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorExpresionIdClase).toString()));
        }
    }

    private void varLocal() throws Exception {
        _inicioVarLocal();
        _restoVarLocal();
    }

    private void _inicioVarLocal() throws Exception {
        match(reservedVar);
        match(idMetVar);
    }

    private void _restoVarLocal() throws Exception {
        match(assignOp);
        expresionCompuesta();
        _operadorTernario();
    }

    private void _operadorTernario() throws Exception {
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
        match(assignOp);
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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(OperadorBinario).toString()));
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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(ExpresionBasica).toString()));
        }
    }

    private void operadorUnario() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorUnario, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(OperadorUnario).toString()));
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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Operando).toString()));
        }
    }

    private void primitivo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Primitivo, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Primitivo).toString()));
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
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_Encadenado).toString()));
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
        else if (firsts.containsToken(_OperadorTernario, currentTokenType)) {
            _operadorTernario();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Primario).toString()));
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
        _tipoParametricoInstanciacion();
        argsActuales();
    }

    private void _tipoParametricoInstanciacion() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(lesserOp)) {
            match(lesserOp);
            _parametrosGenericidad();
        }
        else { /* epsilon */ }
    }

    private void _parametrosGenericidad() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(greaterOp)) {
            match(greaterOp);
        }
        else if (currentTokenType.equals(idClase)) {
            match(idClase);
            match(greaterOp);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_ParametrosGenericidad).toString()));
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
    private void _forStatement() throws Exception {
        match(reservedFor);
        match(openParenthesis);
        _restoFor();
    }

    private void _restoFor() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();

        if (firsts.containsToken(Tipo, currentTokenType)) {
            tipo();
            match(idMetVar);
            _decisorForTipo();
        }
        else if (currentTokenType.equals(reservedVar)) {
            _inicioVarLocal();
            _decisorForVarLocal();
        }
        else if (firsts.containsToken(_ForEstandar, currentTokenType)) {
            _forEstandar();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_RestoFor).toString()));
        }
    }

    private void _decisorForTipo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(colon)) {
            match(colon);
            match(idMetVar);
            match(closeParenthesis);
            bloqueOpcional();
        }
        else if (currentTokenType.equals(semicolon)) {
            match(semicolon);
            expresionOpcional();
            match(semicolon);
            _incrementoOpcional();
            match(closeParenthesis);
            bloqueOpcional();
        }
        else if (currentTokenType.equals(assignOp)) {
            _restoVarLocal();
            match(semicolon);
            expresionOpcional();
            match(semicolon);
            _incrementoOpcional();
            match(closeParenthesis);
            bloqueOpcional();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorForTipo).toString()));
        }
    }

    private void _decisorForVarLocal() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(colon)) {
            match(colon);
            match(idMetVar);
            match(closeParenthesis);
            bloqueOpcional();
        }
        else if(currentTokenType.equals(assignOp)) {
            _restoVarLocal();
            match(semicolon);
            expresionOpcional();
            match(semicolon);
            _incrementoOpcional();
            match(closeParenthesis);
            bloqueOpcional();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorForVarLocal).toString()));
        }
    }

    private void _forEstandar() throws Exception {
        _declaracionOpcional();
        match(semicolon);
        expresionOpcional();
        match(semicolon);
        _incrementoOpcional();
        match(closeParenthesis);
        bloqueOpcional();
    }

    private void _restoIdMetVarForEstandar() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(assignOp)) {
            match(assignOp);
            expresionCompuesta();
        }
        else if (firsts.containsToken(_OperadorUnarioModificador, currentTokenType)) {
            _operadorUnarioModificador();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_ForEstandar).toString()));
        }
    }

    private void _incrementoOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(idMetVar)) {
            match(idMetVar);
            _restoIdMetVarForEstandar();
        }
        else if (firsts.containsToken(_OperadorUnarioModificador, currentTokenType)) {
            expresionBasica();
        }
        else {
            /* epsilon */
        }
    }

    private void _operadorUnarioModificador() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_OperadorUnarioModificador, currentTokenType)) {
            match(currentTokenType);
        }
    }

    private void _declaracionTipoOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Tipo, currentTokenType)) {
            tipo();
            _tipoParametricoOpcional();
        }
        else { /* epsilon */ }
    }

    private void _declaracionOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(_DeclaracionOpcional, currentTokenType)) {
            _declaracionTipoOpcional();
            match(idMetVar);
            _asignacionOpcional();
        }
        else { /* epsilon */ }
    }

    // Opcional Variables Locales Clásicas E2
    private void _varLocalClasica() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(firsts.containsToken(Tipo, currentTokenType)) {
            tipo();
            _tipoParametricoOpcional();
            match(idMetVar);
            _restoVarLocalClasica();
            _asignacionOpcional();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(VarLocal).toString()));
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
            expresionCompuesta();
            _operadorTernario();
        }
        else { /* epsilon */ }
    }
}