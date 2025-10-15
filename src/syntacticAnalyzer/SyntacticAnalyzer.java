package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import model.AST.*;
import model.Firsts;
import model.Following;
import model.Token;
import model.TokenType;
import model.symbolTable.*;
import utils.exceptions.SemanticException;
import utils.exceptions.SyntacticException;
import utils.messages.SemanticErrorIMessage;
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
        Token modifier, name;
        MainElement parent;
        AbstractType parametricType;

        modifier = modificadorOpcional();
        match(reservedInterface);
        name = currentToken;
        match(idClase);
        parametricType = _tipoParametricoOpcional();

        parent = _optionalParent();

        if ((parent instanceof Class) && parent.getName().getLexeme().equals("Object")) {
            parent = null;
        }

        toReturn = new Interface(modifier, name, parametricType, (parent != null ? parent.getName() : null));
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
        AbstractType methodType;
        AbstractType parametricType;

        visibility = _visibilidadOpcional();
        methodType = tipoMetodo();
        parametricType = _tipoParametricoOpcional();
        name = currentToken;

        if(parametricType != null && methodType instanceof ClassType)
            ((ClassType) methodType).setParametricType(parametricType);
            //methodType.setParametricType(parametricType);

        Method newMethod = new Method(name, visibility, null, methodType, true);
        symbolTable.setCurrentService(newMethod);
        symbolTable.getCurrentClass().addMethod(name, newMethod);

        match(idMetVar);
        argsFormales();
        match(semicolon);
    }

    private void clase() throws Exception {
        Class newClass;
        Token modifier, name;
        MainElement parent;
        AbstractType parametricType;

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

    private MainElement _optionalParent() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        MainElement toReturn;
        if (firsts.containsToken(HerenciaOpcional, currentTokenType)) {
            toReturn = herenciaOpcional();
        }
        else if (firsts.containsToken(_InterfaceOpcional, currentTokenType)) {
            toReturn = _interfaceOpcional();
        }
        else { toReturn = symbolTable.getObjectClass(); }
        return toReturn;
    }

    private AbstractType _tipoParametricoOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        AbstractType toReturn;
        if(firsts.containsToken(_TipoParametricoOpcional, currentTokenType)) {
            match(lesserOp);
            toReturn = new ClassType(currentToken);
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

    private Class herenciaOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Class toReturn;
        if (firsts.containsToken(HerenciaOpcional, currentTokenType)) {
            match(reservedExtends);
            toReturn = new Class(null, currentToken, null, null);
            match(idClase);
            AbstractType parentType = _tipoParametricoOpcional();
            toReturn.setParametricType(parentType);
        }
        else { toReturn = null; }
        return toReturn;
    }

    private Interface _interfaceOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Interface toReturn = null;
        if (firsts.containsToken(_InterfaceOpcional, currentTokenType)) {
            match(reservedImplements);
            //toReturn = currentToken;
            toReturn = new Interface(null, currentToken, null, null);
            match(idClase);
            AbstractType parentType = _tipoParametricoOpcional();
            toReturn.setParametricType(parentType);
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
            AbstractType t = new VoidType(currentToken);
            match(reservedVoid);
            Token n = currentToken;
            match(idMetVar);

            Method newMethod = new Method(n, v, null, t);
            if(symbolTable().getCurrentClass().getMethods().contains(newMethod.getName().getLexeme()))
                throw new SemanticException(SemanticErrorIMessage.methodAlreadyExists(newMethod.getName()));

            symbolTable.getCurrentClass().addMethod(n, newMethod);
            symbolTable.setCurrentService(newMethod);

            argsFormales();
            NodoBloque b = bloqueOpcional();
            newMethod.setBloque(b);
        }
        else if (firsts.containsToken(_Modificador, currentTokenType)) {
            Token m = _modificador();
            AbstractType mt = tipoMetodo();
            AbstractType pt = _tipoParametricoOpcional();
            Token n = currentToken;
            match(idMetVar);

            if(pt != null)
                if(mt instanceof ClassType)
                    ((ClassType) mt).setParametricType(pt);
                else
                    throw new SemanticException(SemanticErrorIMessage.parametricTypeNotAllowed(pt.getName()));

            Method newMethod = new Method(n, v, m, mt);
            if(symbolTable().getCurrentClass().getMethods().contains(newMethod.getName().getLexeme()))
                throw new SemanticException(SemanticErrorIMessage.methodAlreadyExists(newMethod.getName()));

            symbolTable.getCurrentClass().addMethod(n, newMethod);
            symbolTable.setCurrentService(newMethod);

            argsFormales();
            NodoBloque b = bloqueOpcional();
            newMethod.setBloque(b);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Miembro).toString()));
        }
    }

    private void _decisorMiembroTipo(Token v) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(currentTokenType.equals(idClase)) {
            AbstractType t = new ClassType(currentToken);
            match(idClase);
            _decisorMiembroIdClase(v, (ClassType) t);
        }
        else if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            AbstractType t = tipoPrimitivo();
            Token n = currentToken;
            match(idMetVar);
            _restoMiembro(t, n);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorMiembroTipo).toString()));
        }
    }

    private void _decisorMiembroIdClase(Token v, ClassType t) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if(firsts.containsToken(ArgsFormales, currentTokenType)) {
            Service s = new Builder(t.getName(), v);
            symbolTable.setCurrentService(s);

            if(symbolTable.getCurrentClass() instanceof Class) {
                Class c = (Class) symbolTable.getCurrentClass();
                c.addConstructor(t.getName(), s);

                argsFormales();
                NodoBloque b = bloque();
                s.setBloque(b);
            }
            else
                throw new SemanticException(SemanticErrorIMessage.constructorFoundInInterface(t.getName()));
        }
        else if (firsts.containsToken(_TipoParametricoOpcional, currentTokenType)) {
            AbstractType pt = _tipoParametricoOpcional();
            Token n = currentToken;
            match(idMetVar);

            if(pt != null && t != null)
                ((ClassType) t).setParametricType(pt);
                //t.setParametricType(pt);

            _restoMiembro(pt, n);
        }
        else if (currentTokenType.equals(idMetVar)) {
            Token n = currentToken;
            match(idMetVar);
            _restoMiembro(t, n);
        }
        else if (currentTokenType.equals(dot)) {
            _encadenado();
            _asignacionOpcional();
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
            _inicializacionAtributoOpcional();
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
                    throw new SemanticException(SemanticErrorIMessage.methodAlreadyExists(s.getName()));

                c.addMethod(n, s);

                argsFormales();
                NodoBloque b = bloque();
                s.setBloque(b);
            }
            else
                throw new SemanticException(SemanticErrorIMessage.constructorFoundInInterface(t.getName()));
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

    private AbstractType tipoMetodo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        AbstractType toReturn; // = new MethodType(currentToken);
        if (firsts.containsToken(Tipo, currentTokenType)) {
            toReturn = tipo();
        }
        else if (currentTokenType.equals(reservedVoid)) {
            toReturn = new VoidType(currentToken);
            match(reservedVoid);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(TipoMetodo).toString()));
        }
        return toReturn;
    }

    private AbstractType tipo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        AbstractType toReturn; // = new MethodType(currentToken);
        if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            toReturn = tipoPrimitivo();
        }
        else if (currentTokenType.equals(idClase)) {
            toReturn = new ClassType(currentToken);
            match(idClase);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Tipo).toString()));
        }
        return toReturn;
    }

    private AbstractType tipoPrimitivo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        AbstractType toReturn; // = new Type(currentToken);
        if (firsts.containsToken(TipoPrimitivo, currentTokenType)) {
            if(currentTokenType.equals(reservedVoid))
                toReturn = new VoidType(currentToken);
            else if (currentTokenType.equals(reservedInt))
                toReturn = new IntType(currentToken);
            else if (currentTokenType.equals(reservedBoolean))
                toReturn = new BooleanType(currentToken);
            else // if (currentTokenType.equals(reservedChar))
                toReturn = new CharType(currentToken);
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
        AbstractType t = tipo();
        AbstractType pT = _tipoParametricoOpcional();

        if(pT != null && t instanceof ClassType)
            ((ClassType) t).setParametricType(pT);

        Token name = currentToken;
        match(idMetVar);

        Parameter newParameter = new Parameter(name, t);
        symbolTable.getCurrentService().addParameter(newParameter);
    }

    private NodoBloque bloqueOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoBloque toReturn;
        if (firsts.containsToken(Bloque, currentTokenType)) {
            toReturn = bloque();
        }
        else if (currentTokenType.equals(semicolon)) {
            match(semicolon);
            toReturn = new NodoBloqueVacio();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(BloqueOpcional).toString()));
        }
        return toReturn;
    }

    private NodoBloque bloque() throws Exception {
        NodoBloque toReturn = new NodoBloque();
        match(openBracket);
        if(symbolTable.getCurrentService() instanceof Method)
            ((Method) symbolTable.getCurrentService()).setCompletedBody();
        listaSentencias(toReturn);
        match(closeBracket);
        return toReturn;
    }

    private NodoBloque listaSentencias(NodoBloque bloque) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(Sentencia, currentTokenType)) {
            NodoSentencia s = sentencia();
            bloque.addStatement(s);
            listaSentencias(bloque);
        }
        else { /* epsilon */ }
        return bloque;
    }

    private NodoSentencia sentencia() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoSentencia toReturn = new NodoSentenciaVacia(); //TODO - sacar null
        if (currentTokenType.equals(semicolon)) {
            match(semicolon);
            toReturn = new NodoSentenciaVacia();
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
            toReturn = bloque();
        }
        else if (firsts.containsToken(If, currentTokenType)) {
            toReturn = ifStatement();
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
        return toReturn;
    }

    private void _decisorExpresionIdClase() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(dot)) {
            match(dot);
            match(idMetVar);
            argsActuales();
            _restoEncadenado();
            _asignacionOpcional();
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

    private NodoSentencia ifStatement() throws Exception {
        NodoIf toReturn;
        match(reservedIf);
        match(openParenthesis);
        NodoExpresion expresion = expresion();
        match(closeParenthesis);
        NodoSentencia sentenciaIf = sentencia();
        NodoSentencia sentenciaElse = _restoIfStatement();
        toReturn = new NodoIf(expresion, sentenciaIf, sentenciaElse);
        return toReturn;
    }

    private NodoSentencia _restoIfStatement() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoSentencia toReturn;
        if (currentTokenType.equals(reservedElse)) {
            match(reservedElse);
            toReturn = sentencia();
        }
        else { /* epsilon */
            toReturn = new NodoSentenciaVacia();
        }
        return toReturn;
    }

    private void whileStatement() throws Exception {
        match(reservedWhile);
        match(openParenthesis);
        expresion();
        match(closeParenthesis);
        sentencia();
    }

    private NodoExpresion expresion() throws Exception {
        NodoExpresion e = expresionCompuesta();
        _restoExpresion();
        return e;
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

    private NodoExpresion expresionCompuesta() throws Exception {
        NodoExpresion e = expresionBasica();
        _restoExpresionCompuesta();
        return e;
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

    private NodoExpresion expresionBasica() throws Exception {
        NodoExpresion toReturn = new NodoExpresionVacia(); //TODO - esto es un mock, borrar
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorUnario, currentTokenType)) {
            operadorUnario();
            operando();
        }
        else if (firsts.containsToken(Operando, currentTokenType)) {
            toReturn = operando();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(ExpresionBasica).toString()));
        }
        return toReturn;
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

    private NodoExpresion operando() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = new NodoExpresionVacia(); //TODO - borrar porque es un mock
        if (firsts.containsToken(Primitivo, currentTokenType)) {
            toReturn = primitivo();
        }
        else if (firsts.containsToken(Referencia, currentTokenType)) {
            referencia();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Operando).toString()));
        }
        return toReturn;
    }

    private NodoExpresion primitivo() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoOperando toReturn;
        if (firsts.containsToken(Primitivo, currentTokenType)) {
            if(currentTokenType.equals(boolFalse) || currentTokenType.equals(boolTrue)) {
                toReturn = new NodoBooleanLiteral(currentToken);
            }
            else if(currentTokenType.equals(intLiteral)){
                toReturn = new NodoIntLiteral(currentToken);
            }
            else if(currentTokenType.equals(charLiteral)){
                toReturn = new NodoCharLiteral(currentToken);
            }
            else {
                toReturn = new NodoNull();
            }
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Primitivo).toString()));
        }
        return toReturn;
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
            _restoEncadenado();
        }
        else if(currentTokenType.equals(dot)) {
            match(dot);
            match(idMetVar);
            _restoEncadenado();
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
            match(semicolon);
        }
        else { /* epsilon */ }
    }
}