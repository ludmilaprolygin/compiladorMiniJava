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
    private void followingListaMiembros() {  //TODO - preguntar ; initEntry(ListaMiembros, firsts.get(ListaMiembros));
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
    private void followingListaSentencias() { //TODO - preguntar ; initEntry(ListaSentencias, firsts.get(ListaSentencias));
        initEntry(ListaSentencias, closeBracket);
        //manageEpsilon(ListaSentencias, ListaSentencias); //TODO - preguntar ; no tiene sentido hacer esto
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
}
