package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import model.AST.Encadenados.Encadenado;
import model.AST.Encadenados.EncadenadoVacio;
import model.AST.Encadenados.NodoLLamadaEncadenada;
import model.AST.Encadenados.NodoVarEncadenada;
import model.AST.Expresiones.*;
import model.AST.Operandos.*;
import model.AST.Sentencias.*;
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

import java.util.LinkedList;

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

        Object[] herencia = _optionalParent();
        parent = (MainElement) herencia[0];
        char c = (char) herencia[1];

        if ((parent instanceof Class) && parent.getName().getLexeme().equals("Object")) {
            parent = null;
            toReturn = new Interface(modifier, name, parametricType, null, c);
        }
        else
        {
            toReturn = new Interface(modifier, name, parametricType, (parent != null ? parent.getName() : null));
        }

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
        Object[] herencia = _optionalParent();
        parent = (MainElement) herencia[0];
        char c = (char) herencia[1];

        newClass = new Class(modifier, name, parametricType, parent, c);
        symbolTable.addClass(name, newClass);
        symbolTable.setCurrentClass(newClass);

        match(openBracket);
        listaMiembros();
        match(closeBracket);
    }

    private Object[] _optionalParent() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        MainElement toReturn;
        Character c;
        Object[] parent;
        if (firsts.containsToken(HerenciaOpcional, currentTokenType)) {
            toReturn = herenciaOpcional();
            c = 'e';
        }
        else if (firsts.containsToken(_InterfaceOpcional, currentTokenType)) {
            toReturn = _interfaceOpcional();
            c = 'i';
        }
        else {
            toReturn = symbolTable.getObjectClass();
            c = 'e';
        }
        parent = new Object[]{toReturn, c};
        return parent;
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
            b.setBloqueContenedor(new NodoBloqueVacio());
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
            NodoExpresion exp = _decisorMiembroIdClase(v, (ClassType) t);
            if(exp instanceof NodoLLamadaMetodoEstatico)
                symbolTable.getBloque().addStatement(new NodoSentenciaConExpresion(exp));
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

    private NodoExpresion _decisorMiembroIdClase(Token v, ClassType t) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = new NodoExpresionVacia();
        if(firsts.containsToken(ArgsFormales, currentTokenType)) {
            Service s = new Builder(t.getName(), v);
            symbolTable.setCurrentService(s);

            if(symbolTable.getCurrentClass() instanceof Class) {
                Class c = (Class) symbolTable.getCurrentClass();
                c.addConstructor(t.getName(), s);

                argsFormales();
                NodoBloque b = bloque();
                b.setBloqueContenedor(new NodoBloqueVacio());
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
            Token idClaseT = t.getName();
            Encadenado e = _encadenado();
            java.util.List<NodoExpresion> args = new LinkedList<>();
            if(e instanceof NodoLLamadaEncadenada)
                args = ((NodoLLamadaEncadenada) e).getParametros();
            Token idMetVarT = e.getNombre();
            toReturn = new NodoLLamadaMetodoEstatico(idClaseT, idMetVarT, args);
            _asignacionOpcional(new NodoExpresionVacia());
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorMiembroIdClase).toString()));
        }
        return toReturn;
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
                b.setBloqueContenedor(new NodoBloqueVacio());
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
            NodoExpresion expresion = expresionCompuesta();
            expresion = _operadorTernario(expresion);
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
            toReturn = new NodoBloqueVacio(symbolTable.getBloque());
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(BloqueOpcional).toString()));
        }
        return toReturn;
    }

    private NodoBloque bloque() throws Exception {
        NodoBloque toReturn = new NodoBloque(symbolTable.getBloque());
        match(openBracket);
        if(symbolTable.getCurrentService() instanceof Method)
            ((Method) symbolTable.getCurrentService()).setCompletedBody();
        //toReturn.setBloqueContenedor(symbolTable.getBloque());
        symbolTable.setBloque(toReturn);
        listaSentencias(toReturn);
        symbolTable.setBloque(toReturn.getBloqueContenedor());
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
        NodoSentencia toReturn = new NodoSentenciaVacia();
        if (currentTokenType.equals(semicolon)) {
            match(semicolon);
            toReturn = new NodoSentenciaVacia();
        }
        else if (currentTokenType.equals(idClase)) {
            Token token = currentToken;
            match(idClase);
            toReturn = _decisorExpresionIdClase(token);
        }
        else if (firsts.containsToken(Expresion, currentTokenType)) {
            toReturn = new NodoSentenciaConExpresion(expresion());
            match(semicolon);
        }
        else if (firsts.containsToken(_VarLocalClasica, currentTokenType)) {
            toReturn = _varLocalClasica();
            match(semicolon);
        }
        else if (firsts.containsToken(VarLocal, currentTokenType)) {
            toReturn = varLocal();
            match(semicolon);
        }
        else if (firsts.containsToken(Return, currentTokenType)) {
            toReturn = returnStatement();
            match(semicolon);
        }
        else if (firsts.containsToken(Bloque, currentTokenType)) {
            toReturn = bloque();
        }
        else if (firsts.containsToken(If, currentTokenType)) {
            toReturn = ifStatement();
        }
        else if (firsts.containsToken(While, currentTokenType)) {
            toReturn = whileStatement();
        }
        else if (firsts.containsToken(_ForStatement, currentTokenType)) {
            _forStatement();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Sentencia).toString()));
        }
        return toReturn;
    }

    private NodoSentencia _decisorExpresionIdClase(Token token) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoSentencia toReturn = new NodoSentenciaVacia();
        if (currentTokenType.equals(dot)) {
            match(dot);
            match(idMetVar);
            java.util.List<NodoExpresion> args = argsActuales();
            _restoEncadenado(new EncadenadoVacio());
            _asignacionOpcional(new NodoExpresionVacia());
        }
        else if (currentTokenType.equals(comma)) {
            _restoVarLocalClasica();
        }
        else if (firsts.containsToken(ExpresionBasica, currentTokenType)) {
            Token t = currentToken;
            NodoExpresion e = expresionBasica();
            NodoExpresion v = new NodoVar(t, new ClassType(token));
            ((NodoVar) v).declare();
            _restoVarLocalClasica();
            v = _restoExpresion(v);
            v = _operadorTernario(v);
            match(semicolon);
            toReturn = new NodoSentenciaConExpresion(v);
        }
        else if (currentTokenType.equals(lesserOp)) {
            _tipoParametricoOpcional();
            match(idMetVar);
            _restoVarLocalClasica();
            _asignacionOpcional(new NodoExpresionVacia());

        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_DecisorExpresionIdClase).toString()));
        }
        return toReturn;
    }

    private NodoSentencia varLocal() throws Exception {
        NodoExpresion expresion;
        NodoVar e = _inicioVarLocal();
        expresion = _restoVarLocal(e);
        e.setTipo(expresion.check());
        symbolTable().getBloque().addVariable(e);
        return new NodoSentenciaConExpresion(expresion);
    }

    private NodoVar _inicioVarLocal() throws Exception {
        NodoVar toReturn;
        match(reservedVar);
        toReturn = new NodoVar(currentToken);
        toReturn.declare();
        toReturn.checkExistance();
        match(idMetVar);
        return toReturn;
    }

    private NodoExpresion _restoVarLocal(NodoExpresion ladoIzquierdo) throws Exception {
        NodoExpresion toReturn;
        Token o = currentToken;
        match(assignOp);
        NodoExpresion expresion = expresionCompuesta();
        expresion = _operadorTernario(expresion);
        toReturn = new NodoExpresionAsignacion(ladoIzquierdo, expresion, o);
        return toReturn;
    }

    private NodoExpresion _operadorTernario(NodoExpresion e) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = e;
        if(currentTokenType.equals(questionMark)) {
            e.check().compatible(new BooleanType(null));
            Token t = currentToken;
            match(questionMark);
            NodoExpresion expTrue = expresion();
            AbstractType expTrueType = expTrue.check();
            match(colon);
            NodoExpresion expFalse = expresion();
            AbstractType expFalseType = expFalse.check();
            expTrueType.compatible(expFalseType);
            toReturn = new NodoExpresionTernaria(t, e, expTrue, expFalse);
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private NodoSentencia returnStatement() throws Exception {
        NodoSentencia toReturn;
        match(reservedReturn);
        NodoExpresion expresion = expresionOpcional();
        toReturn = new NodoReturn(expresion);
        return toReturn;
    }

    private NodoExpresion expresionOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn;
        if (firsts.containsToken(Expresion, currentTokenType)) {
            toReturn = expresion();
        }
        else { /* epsilon */
            toReturn = new NodoExpresionVacia();
        }
        return toReturn;
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

    private NodoSentencia whileStatement() throws Exception {
        NodoSentencia toReturn;
        match(reservedWhile);
        match(openParenthesis);
        NodoExpresion expresion = expresion();
        match(closeParenthesis);
        NodoSentencia sentencia = sentencia();
        toReturn = new NodoWhile(expresion, sentencia);
        return toReturn;
    }

    private NodoExpresion expresion() throws Exception {
        NodoExpresion e = expresionCompuesta();
        e = _restoExpresion(e);
        return e;
    }

    private NodoExpresion _restoExpresion(NodoExpresion ladoIzquierdo) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = ladoIzquierdo;
        if (firsts.containsToken(OperadorAsignacion, currentTokenType)) {
            Token o = operadorAsignacion();
            NodoExpresion ladoDerecho = expresionCompuesta();
            toReturn = new NodoExpresionAsignacion(ladoIzquierdo, ladoDerecho, o);
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private Token operadorAsignacion() throws Exception {
        Token o = currentToken;
        match(assignOp);
        return o;
    }

    private NodoExpresion expresionCompuesta() throws Exception {
        NodoExpresion e = expresionBasica();
        e = _restoExpresionCompuesta(e);
        return e;
    }

    private NodoExpresion _restoExpresionCompuesta(NodoExpresion ladoIzquierdo) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = ladoIzquierdo;
        if (firsts.containsToken(OperadorBinario, currentTokenType)) {
            Token operador = operadorBinario();
            NodoExpresion ladoDerecho = expresionBasica();
            ladoDerecho = _restoExpresionCompuesta(ladoDerecho);
            toReturn = new NodoExpresionBinaria(operador, ladoIzquierdo, ladoDerecho);

        }
        else { /* epsilon */ }
        return toReturn;
    }

    private Token operadorBinario() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn = currentToken;
        if (firsts.containsToken(OperadorBinario, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(OperadorBinario).toString()));
        }
        return toReturn;
    }

    private NodoExpresion expresionBasica() throws Exception {
        NodoExpresion toReturn;
        TokenType currentTokenType = getCurrentTokenType();
        if (firsts.containsToken(OperadorUnario, currentTokenType)) {
            Token operador = operadorUnario();
            NodoExpresion expresion = operando();
            toReturn = new NodoExpresionUnaria(operador, expresion);
        }
        else if (firsts.containsToken(Operando, currentTokenType)) {
            toReturn = operando();
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(ExpresionBasica).toString()));
        }
        return toReturn;
    }

    private Token operadorUnario() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Token toReturn = currentToken;
        if (firsts.containsToken(OperadorUnario, currentTokenType)) {
            match(currentTokenType);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(OperadorUnario).toString()));
        }
        return toReturn;
    }

    private NodoExpresion operando() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = new NodoExpresionVacia();
        if (firsts.containsToken(Primitivo, currentTokenType)) {
            toReturn = primitivo();
        }
        else if (firsts.containsToken(Referencia, currentTokenType)) {
            toReturn = referencia();
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

    private NodoExpresion referencia() throws Exception {
        NodoExpresion var = primario();
        Encadenado e = _restoReferencia();

        if(var instanceof NodoVar){
            NodoVar v = (NodoVar) var;
            v.setEncadenado(e);
            return v;
        }
        if(var instanceof NodoLLamadaMetodo){
            NodoLLamadaMetodo m = (NodoLLamadaMetodo) var;
            m.setEncadenado(e);
            return m;
        }
        if(var instanceof NodoThis){
            NodoThis t = (NodoThis) var;
            t.setEncadenado(e);
            return t;
        }
        if(var instanceof NodoLLamadaConstructor){
            NodoLLamadaConstructor c = (NodoLLamadaConstructor) var;
            c.setEncadenado(e);
            return c;
        }
        return var;
    }

    private Encadenado _restoReferencia() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Encadenado toReturn = null;
        if (firsts.containsToken(_Encadenado, currentTokenType)) {
            toReturn = _encadenado();
            _restoReferencia();
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private Encadenado _encadenado () throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Encadenado toReturn = new EncadenadoVacio();
        if (currentTokenType.equals(dot)) {
            Token token;
            match(dot);
            token = currentToken;
            match(idMetVar);
            toReturn = new NodoVarEncadenada(token);
            Encadenado e = _restoEncadenado(toReturn);

            if(toReturn != e)
                toReturn.setEncadenado(e);
            else
                toReturn = e;

        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(_Encadenado).toString()));
        }
        return toReturn;
    }

    private Encadenado _restoEncadenado (Encadenado encadenado) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        Encadenado toReturn = encadenado;
        if (firsts.containsToken(ArgsActuales, currentTokenType)) {
            Token token = currentToken;
            java.util.List<NodoExpresion> args = argsActuales();
            toReturn = new NodoLLamadaEncadenada(encadenado.getNombre(), null, args);
            //toReturn.setEncadenado(new NodoLLamadaEncadenada(token, null, args));
            Encadenado e = _restoEncadenado(encadenado);
            toReturn.getEncadenado().setEncadenado(e);
        }
        else if(currentTokenType.equals(dot)) {
            Token token;
            match(dot);
            token = currentToken;
            match(idMetVar);
            NodoVarEncadenada var = new NodoVarEncadenada(token);
            Encadenado e = _restoEncadenado(var);
            toReturn.setEncadenado(e);
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private NodoExpresion primario() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = new NodoExpresionVacia();
        if (currentTokenType.equals(reservedThis)) {
            toReturn = new NodoThis();
            match(currentTokenType);
        } else if (currentTokenType.equals(stringLiteral)) {
            toReturn = new NodoStringLiteral(currentToken);
            match(currentTokenType);
        }
        else if (firsts.containsToken(LlamadaConstructor, currentTokenType)) {
            toReturn = llamadaConstructor();
        }
        else if (currentTokenType.equals(idMetVar)) {
            toReturn = new NodoVar(currentToken);
            match(idMetVar);
            toReturn = _restoLlamadaMetodo(toReturn);
        }
        else if (firsts.containsToken(LlamadaMetodoEstatico, currentTokenType)) {
            toReturn = llamadaMetodoEstatico();
        }
        else if (firsts.containsToken(ExpresionParentizada, currentTokenType)) {
            toReturn = expresionParentizada();
        }
        else if (firsts.containsToken(_OperadorTernario, currentTokenType)) {
            _operadorTernario(new NodoExpresionVacia());
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(Primario).toString()));
        }
        return toReturn;
    }

    private NodoExpresion _restoLlamadaMetodo(NodoExpresion e) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = e;
        if (firsts.containsToken(ArgsActuales, currentTokenType)) {
            java.util.List<NodoExpresion> args = argsActuales();
            toReturn = new NodoLLamadaMetodo(((NodoVar) e).getToken(), args, symbolTable.getCurrentClass());
            symbolTable.getBloque().addLlamada(toReturn);
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private NodoLLamadaConstructor llamadaConstructor() throws Exception {
        match(reservedNew);
        ClassType t = new ClassType(currentToken);
        match(idClase);
        _tipoParametricoInstanciacion();
        java.util.List<NodoExpresion> a = argsActuales();
        NodoLLamadaConstructor toReturn = new NodoLLamadaConstructor(t, a);
        return toReturn;
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

    private NodoLLamadaMetodoEstatico llamadaMetodoEstatico() throws Exception {
        NodoLLamadaMetodoEstatico toReturn;
        Token idClaseT = currentToken;
        match(idClase);
        match(dot);
        Token idMetVarT = currentToken;
        match(idMetVar);
        java.util.List<NodoExpresion> args = argsActuales();
        toReturn = new NodoLLamadaMetodoEstatico(idClaseT, idMetVarT, args);
        return toReturn;
    }

    private NodoExpresion expresionParentizada() throws Exception {
        match(openParenthesis);
        NodoExpresion toReturn = expresion();
        match(closeParenthesis);
        return toReturn;
    }

    private java.util.List<NodoExpresion> argsActuales() throws Exception {
        match(openParenthesis);
        java.util.List<NodoExpresion> toReturn = listaExpsOpcional();
        match(closeParenthesis);
        return toReturn;
    }

    private java.util.List<NodoExpresion> listaExpsOpcional() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        java.util.List<NodoExpresion> toReturn = new LinkedList<>();
        if (firsts.containsToken(ListaExps, currentTokenType)) {
            listaExps(toReturn);
        }
        else { /* epsilon */ }
        return toReturn;
    }

    private void listaExps(java.util.List<NodoExpresion> list) throws Exception {
        NodoExpresion e = expresion();
        list.addLast(e);
        _restoListaExps(list);
    }

    private void _restoListaExps(java.util.List<NodoExpresion> list) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        if (currentTokenType.equals(comma)) {
            match(comma);
            listaExps(list);
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
            _restoVarLocal(new NodoExpresionVacia());
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
            _restoVarLocal(new NodoExpresionVacia());
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
            _asignacionOpcional(new NodoExpresionVacia());
        }
        else { /* epsilon */ }
    }

    // Opcional Variables Locales Clásicas E2
    private NodoSentencia _varLocalClasica() throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoSentencia toReturn = new NodoSentenciaVacia();
        if(firsts.containsToken(Tipo, currentTokenType)) {
            NodoVar var;
            AbstractType tipo = tipo();
            _tipoParametricoOpcional();
            var = new NodoVar(currentToken);
            match(idMetVar);
            var.setTipo(tipo);
            _restoVarLocalClasica();
            _asignacionOpcional(new NodoExpresionVacia());
            symbolTable().getBloque().addVariable(var);
        }
        else {
            throw new SyntacticException(SyntacticErrorMessage.basicError(currentToken, firsts.get(VarLocal).toString()));
        }
        return toReturn;
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

    private NodoExpresion _asignacionOpcional(NodoExpresion ladoIzquierdo) throws Exception {
        TokenType currentTokenType = getCurrentTokenType();
        NodoExpresion toReturn = ladoIzquierdo;
        if (firsts.containsToken(OperadorAsignacion, currentTokenType)) {
            Token o = operadorAsignacion();
            NodoExpresion ladoDerecho = expresionCompuesta();
            _operadorTernario(ladoDerecho);
            toReturn = new NodoExpresionAsignacion(ladoIzquierdo, ladoDerecho, o);
            match(semicolon);
        }
        else { /* epsilon */ }
        return toReturn;
    }
}