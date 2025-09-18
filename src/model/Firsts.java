package model;

import static model.SyntacticMethod.*;
import static model.TokenType.*;

public final class Firsts extends NonTerminals {
    public Firsts(){
        super();
        firstsInit();
    }

    private void firstsInit(){
        terminales();
        noTerminales();
        addEpsilon(ListaClases, _TipoParametricoOpcional, ModificadorOpcional, HerenciaOpcional, _InterfaceOpcional, ListaMiembros, _Visibilidad, _InicializacionAtributoOpcional);
        addEpsilon(ListaArgsFormalesOpcional, _RestoListaArgsFormales, ListaSentencias, _OperadorTernario, ExpresionOpcional, _RestoIf, _RestoExpresion, _RestoExpresionCompuesta);
        addEpsilon(_RestoReferencia, _RestoEncadenado, _RestoLlamadaMetodo, ListaExpsOpcional, _RestoListaExps, _DeclaracionOpcional, _AsignacionOpcional, _RestoVarLocalClasica);
        addEpsilon(_IncrementoOpcional, _DeclaracionTipoOpcional, _TipoParametricoInstanciacion);
    }

    private void terminales() {
        initEntry(Inicial, END_OF_FILE);
        initEntry(Clase, reservedClass);
        initEntry(_Modificador, reservedAbstract, reservedStatic, reservedFinal);
        initEntry(Miembro, reservedVoid);
        initEntry(Constructor, idClase);
        initEntry(_Visibilidad, reservedPublic, reservedPrivate);
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
        initEntry(_ParametrosGenericidad, lesserOp);
        initEntry(_ForStatement, reservedFor);
        initEntry(_OperadorUnarioModificador, incrementOp, decrementOp);
        initEntry(_ForEstandar, semicolon);
        initEntry(_DeclaracionOpcional, idMetVar);
        initEntry(_DecisorExpresionIdClase, comma, dot);
        initEntry(_DecisorForVarLocal, colon, assignOp);
        initEntry(_OperadorTernario, questionMark);
        initEntry(_TipoParametricoInstanciacion, lesserOp);
        initEntry(_IncrementoOpcional, idMetVar);
        initEntry(_DecisorMiembroTipo, idClase);
        initEntry(_DecisorMiembroIdClase, idMetVar);
        initEntry(_RestoFor, idMetVar);
        initEntry(_DecisorForTipo, assignOp, semicolon, colon);
    }

    private void noTerminales() {
        initEntry(_IncrementoOpcional, get(_OperadorUnarioModificador));
        initEntry(_RestoMiembro, get(_InicializacionAtributoOpcional));
        initEntry(ModificadorOpcional, get(_Modificador));
        initEntry(ExpresionBasica, get(OperadorUnario));
        initEntry(Tipo, get(TipoPrimitivo));
        initEntry(_DeclaracionTipoOpcional, get(Tipo));
        initEntry(_ForIteradores, get(Tipo));
        initEntry(TipoMetodo, get(Tipo));
        initEntry(_DeclaracionOpcional, get(Tipo));
        initEntry(_ForEstandar, get(_DeclaracionOpcional));
        initEntry(Miembro, get(_Modificador));
        initEntry(Miembro, get(Constructor));
        initEntry(Miembro, get(Tipo));
        initEntry(Miembro, get(_Visibilidad));
        initEntry(_VarLocalClasica, get(Tipo));
        initEntry(ArgFormal, get(Tipo));
        initEntry(_AsignacionOpcional, get(OperadorAsignacion));
        initEntry(Sentencia, get(_VarLocalClasica));
        initEntry(Sentencia, get(VarLocal));
        initEntry(Sentencia, get(Return));
        initEntry(Sentencia, get(If));
        initEntry(Sentencia, get(While));
        initEntry(Sentencia, get(_ForStatement));
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
        initEntry(_DecisorExpresionIdClase, get(ExpresionCompuesta));
        initEntry(Expresion, get(ExpresionCompuesta));
        initEntry(Sentencia, get(Expresion));
        initEntry(_DecisorMiembroTipo, get(TipoPrimitivo));
        initEntry(ExpresionOpcional, get(Expresion));
        initEntry(ListaExps, get(Expresion));
        initEntry(ListaSentencias, get(Sentencia));
        initEntry(ListaExpsOpcional, get(ListaExps));
        initEntry(BloqueOpcional, get(Bloque));
        initEntry(ListaMiembros, get(Miembro));
        initEntry(_MiembroCompleto, get(Miembro));
        initEntry(_MiembroCompleto, get(_Visibilidad));
        initEntry(_DecisorMiembroIdClase, get(_TipoParametricoOpcional));
        initEntry(_DecisorMiembroIdClase, get(ArgsFormales));
        initEntry(Clase, get(ModificadorOpcional));
        initEntry(ListaClases, get(Clase));
        initEntry(Inicial, get(ListaClases));
        initEntry(_RestoFor, get(Tipo));
        initEntry(_RestoFor, get(_ForEstandar));
    }
}
