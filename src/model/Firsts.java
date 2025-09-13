package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static model.SyntacticMethod.*;
import static model.TokenType.*;

public final class Firsts extends HashMap<SyntacticMethod, ArrayList<TokenType>> {
    public Firsts(){
        super();
        mapInit();
        firstsInit();
    }

    private void mapInit(){
        for (SyntacticMethod syntaticMethod: SyntacticMethod.values() ) {
            put(syntaticMethod, new ArrayList<TokenType>());
        }
    }

    private void firstsInit(){
        terminales();
        noTerminales();
    }

    private void terminales() {
        initEntry(Inicial, END_OF_FILE);
        initEntry(Clase, reservedClass);
        initEntry(_Modificador, reservedAbstract, reservedStatic, reservedFinal);
        initEntry(Miembro, reservedVoid);
        initEntry(Constructor, reservedPublic);
        initEntry(TipoMetodo, reservedVoid);
        initEntry(Tipo, idClase);
        initEntry(TipoPrimitivo, reservedBoolean, reservedChar, reservedInt);
        initEntry(ArgsFormales, openParenthesis);
        initEntry(_RestoListaArgsFormales, comma);
        initEntry(Bloque, openBracket);
        initEntry(Sentencia, semicolon);
        initEntry(VarLocal, reservedVar);
        initEntry(Return, reservedReturn);
        initEntry(If, reservedIf);
        initEntry(_RestoIf, reservedElse);
        initEntry(While, reservedWhile);
        initEntry(OperadorAsignacion, assignOp, plusOp, minusOp);
        initEntry(OperadorBinario, orOp, andOp, equalsOp, notEqualOp, greaterOp, lesserOp, greaterEqualOp, lesserEqualOp, plusOp, minusOp, multOp, divOp, modOp);
        initEntry(OperadorUnario, notOp, minusOp, plusOp, incrementOp, decrementOp);
        initEntry(Primitivo, boolTrue, boolFalse, intLiteral, charLiteral, reservedNull);
        initEntry(_Encadenado, dot);
        initEntry(Primario, reservedThis, stringLiteral, idMetVar);
        initEntry(AccesoVar, idMetVar);
        initEntry(LlamadaConstructor, reservedNew);
        initEntry(ExpresionParentizada, openParenthesis);
        initEntry(LlamadaMetodo, idMetVar);
        initEntry(LlamadaMetodoEstatico, idClase);
        initEntry(ArgsActuales, openParenthesis);
        initEntry(_RestoListaExps, comma);
        initEntry(HerenciaOpcional, reservedExtends);
        initEntry(_RestoMiembro, semicolon, openParenthesis);
        initEntry(BloqueOpcional, semicolon);
        initEntry(_RestoVarLocal, questionMark);
        initEntry(_InicializacionAtributoOpcional, assignOp);
        initEntry(_InterfaceOpcional, reservedImplements);
        initEntry(_RestoVarLocalClasica, comma);
        initEntry(_Valor, stringLiteral, intLiteral, charLiteral);
        initEntry(_TipoParametricoOpcional, lesserOp);
    }

    private void noTerminales() {
        initEntry(_RestoMiembro, get(_InicializacionAtributoOpcional));
        initEntry(ModificadorOpcional, get(_Modificador));
        initEntry(ExpresionBasica, get(OperadorUnario));
        initEntry(Tipo, get(TipoPrimitivo));
        initEntry(TipoMetodo, get(Tipo));
        initEntry(Miembro, get(_Modificador));
        initEntry(Miembro, get(Constructor));
        initEntry(Miembro, get(Tipo));
        initEntry(_VarLocalClasica, get(Tipo));
        initEntry(ArgFormal, get(Tipo));
        initEntry(_AsignacionOpcional, get(OperadorAsignacion));
        initEntry(Sentencia, get(_VarLocalClasica));
        initEntry(Sentencia, get(VarLocal));
        initEntry(Sentencia, get(Return));
        initEntry(Sentencia, get(If));
        initEntry(Sentencia, get(While));
        initEntry(ListaArgsFormales, get(ArgFormal));
        initEntry(_RestoExpresion, get(OperadorAsignacion));
        initEntry(_RestoExpresionCompuesta, get(OperadorBinario));
        initEntry(_RestoReferencia, get(_Encadenado));
        initEntry(Primario, get(ExpresionParentizada));
        initEntry(_RestoEncadenado, get(ArgsActuales));
        initEntry(Primario, get(LlamadaConstructor));
        initEntry(Primario, get(LlamadaMetodoEstatico));
        initEntry(_RestoLlamadaMetodo, get(ArgsActuales));
        initEntry(ListaArgsFormalesOpcional, get(ListaArgsFormales));
        initEntry(Sentencia, get(Bloque));
        initEntry(Operando, get(Primitivo));
        initEntry(Referencia, get(Primario));
        initEntry(Operando, get(Referencia));
        initEntry(ExpresionBasica, get(Operando));
        initEntry(ExpresionCompuesta, get(ExpresionBasica));
        initEntry(Expresion, get(ExpresionCompuesta));
        initEntry(Sentencia, get(Expresion));
        initEntry(ExpresionOpcional, get(Expresion));
        initEntry(ListaExps, get(Expresion));
        initEntry(ListaSentencias, get(Sentencia));
        initEntry(ListaExpsOpcional, get(ListaExps));
        initEntry(BloqueOpcional, get(Bloque));
        initEntry(ListaMiembros, get(Miembro));
        initEntry(Clase, get(ModificadorOpcional));
        initEntry(ListaClases, get(Clase));
        initEntry(Inicial, get(ListaClases));
    }
    private void initEntry(SyntacticMethod key, TokenType... firsts){
        get(key).addAll(Arrays.asList(firsts));
    }

    private void initEntry(SyntacticMethod key, List<TokenType> tokens) {
        get(key).addAll(tokens);
    }

    public boolean containsToken(SyntacticMethod key, TokenType token) {
        ArrayList<TokenType> tokens = get(key);
        if (tokens == null) {
            return false;
        }
        return tokens.contains(token);
    }

}
