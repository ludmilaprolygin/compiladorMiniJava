package model;

import static model.SyntacticMethod.*;
import static model.TokenType.*;

public final class Following extends NonTerminals {
    private Firsts firsts;
    public Following(Firsts firsts) {
        super();
        this.firsts = firsts;
        followingInit();
    }

    private void followingInit(){
        followingListaClases();
        followingClase();
        followingTipoParametricoOpcional();
        followingModificadorOpcional();
        followingHerenciaOpcional();
        followingInterfaceOpcional();
        followingListaMiembros();
        followingMiembroCompleto();
        followingVisibilidad();
        followingMiembro();
        followingDecisorMiembroTipo();
        followingDecisorMiembroIdClase();
        followingRestoMiembro();
        followingInicializacionAtributoOpcional();
        followingModificador();
        followingTipoMetodo();
        followingDeclaracionTipoOpcional();
        followingTipo();
        followingTipoPrimitivo();
        followingArgsFormales();
        followingListaArgsFormalesOpcional();
        followingListaArgsFormales();
        followingRestoListaArgsFormales();
        followingArgFormal();
        followingBloqueOpcional();
        followingBloque();
        followingListaSentencias();
        followingIf();
        followingSentencia();
        followingDecisorExpresionIdClase();
        followingTipoClase();
        followingVarLocal();
        followingInicioVarLocal();
        followingRestoVarLocal();
        followingOperadorTernario();
        followingReturnStatement();
        followingExpresionOpcional();
        followingIfStatement();
        followingRestoIfStatement();
        followingWhileStatement();
        followingExpresion();
        followingRestoExpresion();
        followingOperadorAsignacion();
        followingOperadorBinario();
        followingOperadorUnario();
        followingTipoParametricoInstanciacion();
        followingParametrosGenericidad();
        followingListaExpsOpcional();
        followingForStatement();
        followingRestoFor();
        followingDecisorForTipo();
        followingDecisorForVarLocal();
        followingForEstandar();
        followingIncrementoOpcional();
        followingRestoIdMetVarForEstandar();
        followingOperadorUnarioModificador();
        followingDeclaracionOpcional();

        removeEpsilon();
    }

    private void removeEpsilon() {
        for(SyntacticMethod method: SyntacticMethod.values())
            removeEpsilon(method);
    }
    private void manageEpsilon(SyntacticMethod syntacticMethodHeadZ, SyntacticMethod syntacticMethodBodyX) {
        initEntry(syntacticMethodBodyX, get(syntacticMethodHeadZ));
    }
    private void manageEpsilon(SyntacticMethod syntacticMethodHeadZ, SyntacticMethod syntacticMethodBodyX, SyntacticMethod syntacticMethodBodyB) {
        if(firsts.get(syntacticMethodBodyB).contains(EPSILON)){
            initEntry(syntacticMethodBodyX, get(syntacticMethodHeadZ));
        }
    }

    private void followingListaClases() {  //TODO - preguntar ; initEntry(ListaClases, firsts.get(ListaClases));
        initEntry(ListaClases, END_OF_FILE);
    }
    private void followingClase() {
        initEntry(Clase, firsts.get(ListaClases));
        manageEpsilon(ListaClases, Clase);
    }
    private void followingTipoParametricoOpcional() {
        initEntry(_TipoParametricoOpcional, firsts.get(HerenciaOpcional));
        initEntry(_TipoParametricoOpcional, firsts.get(_InterfaceOpcional));
        initEntry(_TipoParametricoOpcional, openBracket);
        initEntry(_TipoParametricoOpcional, idMetVar);
    }
    private void followingModificadorOpcional() {
        initEntry(ModificadorOpcional, reservedClass);
    }
    private void followingHerenciaOpcional() {
        initEntry(_TipoParametricoOpcional, firsts.get(_InterfaceOpcional));
        initEntry(_TipoParametricoOpcional, openBracket);
    }
    private void followingInterfaceOpcional() {
        initEntry(_InterfaceOpcional, openBracket);
    }
    private void followingListaMiembros() {
        initEntry(ListaMiembros, closeBracket);
    }
    private void followingMiembroCompleto() {
        initEntry(_MiembroCompleto, firsts.get(ListaMiembros));
        manageEpsilon(ListaMiembros, _MiembroCompleto);
    }
    private void followingVisibilidad() {
        initEntry(_Visibilidad, firsts.get(Miembro));
        manageEpsilon(_MiembroCompleto, _Visibilidad, Miembro);
    }
    private void followingMiembro() {
        manageEpsilon(_MiembroCompleto, Miembro);
    }
    private void followingDecisorMiembroTipo() {
        manageEpsilon(Miembro, _DecisorMiembroTipo);
    }
    private void followingDecisorMiembroIdClase() {
        manageEpsilon(Miembro, _DecisorMiembroIdClase);
    }
    private void followingRestoMiembro() {
        manageEpsilon(_DecisorMiembroTipo, _RestoMiembro);
        manageEpsilon(_DecisorMiembroIdClase, _RestoMiembro);
    }
    private void followingInicializacionAtributoOpcional() {
        initEntry(_InicializacionAtributoOpcional, semicolon);
    }
    private void followingModificador() {
        initEntry(_Modificador, firsts.get(TipoMetodo));
    }
    private void followingTipoMetodo() {
        initEntry(TipoMetodo, firsts.get(_TipoParametricoOpcional));
        initEntry(TipoMetodo, idMetVar);
    }
    private void followingDeclaracionTipoOpcional() {
        initEntry(_DeclaracionTipoOpcional, idMetVar);
    }
    private void followingTipo() {
        manageEpsilon(TipoMetodo, Tipo);
        initEntry(Tipo, idMetVar);
        manageEpsilon(_DeclaracionTipoOpcional, Tipo);
    }
    private void followingTipoPrimitivo() {
        initEntry(TipoPrimitivo, firsts.get(_TipoParametricoOpcional));
        manageEpsilon(Tipo, TipoPrimitivo);
        initEntry(TipoPrimitivo, idMetVar);
    }
    private void followingArgsFormales() {
        initEntry(ArgsFormales, firsts.get(BloqueOpcional));
        initEntry(ArgsFormales, firsts.get(Bloque));
    }
    private void followingListaArgsFormalesOpcional() {
        initEntry(ListaArgsFormalesOpcional, closeParenthesis);
    }
    private void followingListaArgsFormales() {
        manageEpsilon(ListaArgsFormalesOpcional, ListaArgsFormales);
    }
    private void followingRestoListaArgsFormales() { //TODO - preguntar ; initEntry(_RestoListaArgsFormales, firsts.get(_RestoListaArgsFormales));
        manageEpsilon(ListaArgsFormales, _RestoListaArgsFormales);
    }
    private void followingArgFormal() {
        initEntry(ArgFormal, firsts.get(_RestoListaArgsFormales));
        manageEpsilon(ListaArgsFormales, ArgFormal, _RestoListaArgsFormales);
        manageEpsilon(_RestoListaArgsFormales, ArgFormal, _RestoListaArgsFormales);
    }
    private void followingBloqueOpcional() {
        manageEpsilon(Miembro, BloqueOpcional);
        manageEpsilon(_RestoMiembro, BloqueOpcional);
        manageEpsilon(_DecisorForTipo, BloqueOpcional);
        manageEpsilon(_DecisorForVarLocal, BloqueOpcional);
        manageEpsilon(_ForEstandar, BloqueOpcional);
    }
    private void followingBloque() {
        manageEpsilon(_DecisorMiembroTipo, Bloque);
        manageEpsilon(BloqueOpcional, Bloque);
        manageEpsilon(Sentencia, Bloque);
    }
    private void followingListaSentencias() {
        initEntry(ListaSentencias, closeBracket);
  }
    private void followingIf() {
        manageEpsilon(Sentencia, If);
    }
    private void followingSentencia() {
        initEntry(Sentencia, firsts.get(ListaSentencias));
        manageEpsilon(ListaSentencias, Sentencia, ListaSentencias);
        initEntry(Sentencia, firsts.get(_RestoIf));
        manageEpsilon(If, Sentencia, _RestoIf);
        manageEpsilon(_RestoIf, Sentencia);
        manageEpsilon(While, Sentencia);
    }
    private void followingDecisorExpresionIdClase() {
        manageEpsilon(Sentencia, _DecisorExpresionIdClase);
    }
    private void followingTipoClase() {
        manageEpsilon(_VarLocalClasica, _TipoClase, _TipoParametricoOpcional);
        initEntry(_TipoClase, idMetVar);
    }
    private void followingVarLocal() {
        initEntry(VarLocal, semicolon);
    }
    private void followingInicioVarLocal() {
        initEntry(_InicioVarLocal, firsts.get(_RestoVarLocal));
        manageEpsilon(VarLocal, _InicioVarLocal, _RestoVarLocal);
    }
    private void followingRestoVarLocal() {
        manageEpsilon(VarLocal, _RestoVarLocal);
        initEntry(_RestoVarLocal, semicolon);
    }
    private void followingOperadorTernario() {
        initEntry(_OperadorTernario, semicolon);
        manageEpsilon(_RestoVarLocal, _OperadorTernario);
        manageEpsilon(_AsignacionOpcional, _OperadorTernario);
    }
    private void followingReturnStatement() {
        initEntry(Return, semicolon);
    }
    private void followingExpresionOpcional() {
        manageEpsilon(Return, ExpresionOpcional);
        initEntry(ExpresionOpcional, semicolon);
    }
    private void followingIfStatement() {
        manageEpsilon(Sentencia, If);
    }
    private void followingRestoIfStatement() {
        manageEpsilon(If, _RestoIf);
    }
    private void followingWhileStatement() {
        manageEpsilon(Sentencia, While);
    }
    private void followingExpresion() {
        initEntry(Expresion, semicolon);
        initEntry(Expresion, colon);
        manageEpsilon(_OperadorTernario, Expresion);
        manageEpsilon(ExpresionOpcional, Expresion);
        initEntry(Expresion, closeParenthesis);
        initEntry(Expresion, firsts.get(_RestoListaExps));
        manageEpsilon(ListaExps, Expresion);
    }
    private void followingRestoExpresion() {
        initEntry(_RestoExpresion, firsts.get(_OperadorTernario));
        initEntry(_RestoExpresion, semicolon);
        manageEpsilon(Expresion, _RestoExpresion);
    }
    private void followingOperadorAsignacion() {
        initEntry(OperadorAsignacion, firsts.get(ExpresionCompuesta));
        manageEpsilon(_RestoExpresion, OperadorAsignacion, ExpresionCompuesta);
        initEntry(OperadorAsignacion, firsts.get(ExpresionCompuesta));
    }
    private void followingExpresionCompuesta() {
        initEntry(ExpresionCompuesta, firsts.get(_RestoVarLocalClasica));
        initEntry(ExpresionCompuesta, firsts.get(_RestoExpresion));
        initEntry(ExpresionCompuesta, firsts.get(_OperadorTernario));
        initEntry(ExpresionCompuesta, semicolon);
        manageEpsilon(Expresion, ExpresionCompuesta, _RestoExpresion);
        manageEpsilon(_RestoExpresion, ExpresionCompuesta);
    }
    private void followingOperadorBinario() {
        initEntry(OperadorBinario, firsts.get(ExpresionBasica));
    }
    private void followingOperadorUnario() {
        initEntry(OperadorUnario, firsts.get(Operando));
    }
    private void followingOperando(){
        manageEpsilon(_InicializacionAtributoOpcional, Operando);
    }
    private void followingTipoParametricoInstanciacion() {
        initEntry(_TipoParametricoInstanciacion, firsts.get(ArgsActuales));
    }
    private void followingParametrosGenericidad() {
        manageEpsilon(_TipoParametricoInstanciacion, _ParametrosGenericidad);
    }
    private void followingListaExpsOpcional() {
        initEntry(ListaExpsOpcional, closeParenthesis);
    }
    private void followingForStatement() {
        manageEpsilon(Sentencia, _ForStatement);
    }
    private void followingRestoFor(){
        manageEpsilon(_ForStatement, _RestoFor);
    }
    private void followingDecisorForTipo() {
        manageEpsilon(_RestoFor, _DecisorForTipo);
    }
    private void followingDecisorForVarLocal() {
        manageEpsilon(_RestoFor, _DecisorForVarLocal);
    }
    private void followingForEstandar() {
        manageEpsilon(_RestoFor, _ForEstandar);
    }
    private void followingIncrementoOpcional() {
        initEntry(_IncrementoOpcional, closeParenthesis);
    }
    private void followingRestoIdMetVarForEstandar() {
        manageEpsilon(_IncrementoOpcional, _RestoIdMetVarForEstandar);
    }
    private void followingOperadorUnarioModificador() {
        initEntry(_OperadorUnarioModificador, idMetVar);
        manageEpsilon(_RestoIdMetVarForEstandar, _OperadorUnarioModificador);
    }
    private void followingDeclaracionOpcional() {
        initEntry(_DeclaracionOpcional, semicolon);
    }

}
