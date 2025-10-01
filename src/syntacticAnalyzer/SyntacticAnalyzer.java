package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import model.Firsts;
import model.Following;
import model.Token;
import model.TokenType;
import model.symbolTable.*;
import utils.exceptions.SemanticException;
import utils.exceptions.SyntacticException;
import utils.messages.SemanticErrorMessage;
import utils.messages.SyntacticErrorMessage;

import static model.SyntacticMethod.*;
import static model.TokenType.*;
import static model.symbolTable.SymbolTable.symbolTable;

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
            clase();
            listaClases();
        }
        else if (firsts.containsToken(_Interface, currentTokenType)) {
            _interface();
            listaClases();
        }
        else { /* epsilon */ }
    }

    private void _interface() throws Exception {
        Interface toReturn;
        Token modifier, name, parent;
        Type parametricType;

        modifier = modificadorOpcional();
        match(reservedInterface);
        name = currentToken;
        match(idClase);
        parametricType = _tipoParametricoOpcional();
        parent = _optionalParent();

        if(parent.getLexeme().equals("Object"))
            parent = null; // Las interfaces no extienden de Object

        toReturn = new Interface(modifier, name, parametricType, parent);
        symbolTable.addInterface(toReturn.getName(), toReturn);
        symbolTable.setCurrentClass(toReturn);

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
        Token visibility, name;
        MethodType methodType;
        Type parametricType;

        visibility = _visibilidadOpcional();
        methodType = tipoMetodo();
        parametricType = _tipoParametricoOpcional();
        name = currentToken;

        if(parametricType != null)
            methodType.setParametricType(parametricType);

        Method newMethod = new Method(name, visibility, null, methodType);
        symbolTable.setCurrentService(newMethod);

        match(idMetVar);
        argsFormales();
        match(semicolon);
    }

    private void clase() throws Exception {
        Class newClass;
        Token modifier, name, parent;
        Type parametricType;

        modifier = modificadorOpcional();
        match(reservedClass);
        name = currentToken;
        match(idClase);
        parametricType =_tipoParametricoOpcional();
        parent = _optionalParent();

        newClass = new Class(modifier, name, parametricType, parent);
        symbolTable.addClass(name, newClass);
        symbolTable.setCurrentClass(newClass);

        match(openBracket);
        listaMiembros();
        match(closeBracket);
    }

    private Token _optionalParent() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn;
        if (firsts.containsToken(HerenciaOpcional, currentTokenType)) {
            toReturn = herenciaOpcional();
        }
        else if (firsts.containsToken(_InterfaceOpcional, currentTokenType)) {
            toReturn = _interfaceOpcional();
        }
        else { toReturn = new Token(null, "Object", -1); }
        return toReturn;
    }

    private Type _tipoParametricoOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Type toReturn;
        if(firsts.containsToken(_TipoParametricoOpcional, currentTokenType)) {
            match(lesserOp);
            toReturn = new Type(currentToken);
            match(idClase);
            match(greaterOp);
        }
        else { toReturn = null; }
        return toReturn;
    }

    private Token modificadorOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn;
        if (firsts.containsToken(ModificadorOpcional, currentTokenType)) {
            toReturn = currentToken;
            match(currentTokenType);
        }
        else { toReturn = null; }
        return toReturn;
    }

    private Token herenciaOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn;
        if (firsts.containsToken(HerenciaOpcional, currentTokenType)) {
            match(reservedExtends);
            toReturn = currentToken;
            match(idClase);
            _tipoParametricoOpcional();
        }
        else { toReturn = null; }
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
        Token v = _visibilidadOpcional();
        miembro(v);
    }

    private Token _visibilidadOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn;
        if (firsts.containsToken(_Visibilidad, currentTokenType)) {
            toReturn = currentToken;
            match(currentTokenType);
        }
        else { toReturn = null; }
        return toReturn;
    }

    private void miembro(Token v) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Tipo, currentTokenType)) {
            _decisorMiembroTipo(v);
        }
        else if (currentTokenType.equals(reservedVoid)) {
            MethodType t = new MethodType(currentToken);
            match(reservedVoid);
            Token n = currentToken;
            match(idMetVar);

            Method newMethod = new Method(n, v, null, t);
            if(symbolTable().getCurrentClass().getMethods().contains(newMethod.getName().getLexeme()))
                throw new SemanticException(SemanticErrorMessage.methodAlreadyExists(newMethod.getName()));

            symbolTable.getCurrentClass().addMethod(n, newMethod);
            symbolTable.setCurrentService(newMethod);

            argsFormales();
            bloqueOpcional();
        }
        else if (firsts.containsToken(_Modificador, currentTokenType)) {
            Token m = _modificador();
            MethodType mt = tipoMetodo();
            Type pt = _tipoParametricoOpcional();
            Token n = currentToken;
            match(idMetVar);

            if(pt != null)
                mt.setParametricType(pt);

            Method newMethod = new Method(n, v, m, mt);
            if(symbolTable().getCurrentClass().getMethods().contains(newMethod.getName().getLexeme()))
                throw new SemanticException(SemanticErrorMessage.methodAlreadyExists(newMethod.getName()));

            symbolTable.getCurrentClass().addMethod(n, newMethod);
            symbolTable.setCurrentService(newMethod);

            argsFormales();
            bloqueOpcional();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Miembro).toString()));
        }
    }

    private void _decisorMiembroTipo(Token v) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(idClase)) {
            Type t = new Type(currentToken);
            match(idClase);
            _decisorMiembroIdClase(v, t);
        }
        else if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            PrimitiveType t = tipoPrimitivo();
            Token n = currentToken;
            match(idMetVar);
            _restoMiembro(t, n);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorMiembroTipo).toString()));
        }
    }

    private void _decisorMiembroIdClase(Token v, Type t) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(firsts.containsToken(ArgsFormales, currentTokenType)) {
            Service s = new Constructor(t.getName(), v);
            symbolTable.setCurrentService(s);

            if(symbolTable.getCurrentClass() instanceof Class) {
                Class c = (Class) symbolTable.getCurrentClass();
                c.addConstructor(t.getName(), s);

                argsFormales();
                bloque();
            }
            else
                throw new SemanticException(SemanticErrorMessage.constructorFoundInInterface(t.getName()));
        }
        else if (firsts.containsToken(_TipoParametricoOpcional, currentTokenType)) {
            Type pt = _tipoParametricoOpcional();
            Token n = currentToken;
            match(idMetVar);

            if(pt != null)
                t.setParametricType(pt);

            _restoMiembro(pt, n);
        }
        else if (currentTokenType.equals(idMetVar)) {
            Token n = currentToken;
            match(idMetVar);
            _restoMiembro(t, n);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorMiembroIdClase).toString()));
        }
    }

    private void _restoMiembro(AbstractType t, Token n) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(semicolon)) {
            match(semicolon);
            Attribute a = new Attribute(n, t);
            symbolTable.getCurrentClass().addAttribute(n, a);
        }
        else if (firsts.containsToken(_InicializacionAtributoOpcional, currentTokenType)) {
            _inicializacionAtributoOpcional(); //TODO
            Attribute a = new Attribute(n, t);
            symbolTable.getCurrentClass().addAttribute(n, a);
            match(semicolon);
        }
        else if (firsts.containsToken(ArgsFormales, currentTokenType)) {
            Service s = new Method(n, null, null, t);
            symbolTable.setCurrentService(s);

            if(symbolTable.getCurrentClass() instanceof Class) {
                Class c = (Class) symbolTable.getCurrentClass();
                if(symbolTable().getCurrentClass().getMethods().contains(s.getName().getLexeme()))
                    throw new SemanticException(SemanticErrorMessage.methodAlreadyExists(s.getName()));

                c.addMethod(n, s);

                argsFormales();
                bloque();
            }
            else
                throw new SemanticException(SemanticErrorMessage.constructorFoundInInterface(t.getName()));
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

    private Token _modificador() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn = currentToken;
        if (firsts.containsToken(_Modificador, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_Modificador).toString()));
        }
        return toReturn;
    }

    private MethodType tipoMetodo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        MethodType toReturn = new MethodType(currentToken);
        if (firsts.containsToken(Tipo, currentTokenType)) {
            tipo();
        }
        else if (currentTokenType.equals(reservedVoid)) {
            match(reservedVoid);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(TipoMetodo).toString()));
        }
        return toReturn;
    }

    private Type tipo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Type toReturn = new MethodType(currentToken);
        if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            tipoPrimitivo();
        }
        else if (currentTokenType.equals(idClase)) {
            match(idClase);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Tipo).toString()));
        }
        return toReturn;
    }

    private PrimitiveType tipoPrimitivo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        PrimitiveType toReturn = new PrimitiveType(currentToken);
        if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(TipoPrimitivo).toString()));
        }
        return toReturn;
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
        Type t = tipo();
        Type pT = _tipoParametricoOpcional();

        if(pT != null)
            t.setParametricType(pT);

        Token name = currentToken;
        match(idMetVar);

        Parameter newParameter = new Parameter(name, t);
        symbolTable.getCurrentService().addParameter(newParameter);
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