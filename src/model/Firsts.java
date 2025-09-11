package model;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import model.TokenType;
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
        initEntry(Inicial, get(ListaClases));
        initEntry(Inicial, END_OF_FILE);

        initEntry(ListaClases, get(Clase));

        initEntry(Clase, get(ModificadorOpcional));
        initEntry(Clase, reservedClass);

        initEntry(ModificadorOpcional, get(_Modificador));

        initEntry(HerenciaOpcional, reservedExtends);

        initEntry(ListaMiembros, get(Miembro));

        initEntry(Miembro, get(Tipo));
        initEntry(Miembro, reservedVoid);
        initEntry(Miembro, get(_Modificador));
        initEntry(Miembro, get(Constructor));

        initEntry(Constructor, reservedPublic);

        initEntry(TipoMetodo, get(Tipo));
        initEntry(TipoMetodo, reservedVoid);

        initEntry(Tipo, get(TipoPrimitivo));
        initEntry(Tipo, idClase);

        initEntry(TipoPrimitivo, reservedBoolean, reservedChar, reservedInt);

        initEntry(ArgsFormales, openParenthesis);

        initEntry(ListaArgsFormalesOpcional, get(ListaArgsFormales));

        initEntry(ListaArgsFormales, get(ArgFormal));
        initEntry(_RestoListaArgsFormales, comma);

        initEntry(ArgFormal, get(Tipo));

        initEntry(BloqueOpcional, get(Bloque));

        initEntry(Bloque, openBracket);

        initEntry(ListaSentencias, get(Sentencia));

        initEntry(Sentencia, semicolon);
        initEntry(Sentencia, get(Expresion));
        initEntry(Sentencia, get(VarLocal));
        initEntry(Sentencia, get(Return));
        initEntry(Sentencia, get(If));
        initEntry(Sentencia, get(While));
        initEntry(Sentencia, get(Bloque));

        initEntry(VarLocal, reservedVar);

        initEntry(Return, reservedReturn);

        initEntry(ExpresionOpcional, get(Expresion));

        initEntry(If, reservedIf);
        initEntry(_RestoIf, reservedElse);

        initEntry(While, reservedWhile);

        initEntry(Expresion, get(ExpresionCompuesta));
        initEntry(_RestoExpresion, get(OperadorAsignacion));

        initEntry(OperadorAsignacion, assignOp, plusOp, minusOp);

        initEntry(ExpresionCompuesta, get(ExpresionBasica));
        initEntry(_RestoExpresionCompuesta, get(OperadorBinario));

        initEntry(OperadorBinario, orOp, andOp, equalsOp, notEqualOp, greaterOp, lesserOp, greaterEqualOp, lesserEqualOp, plusOp, minusOp, multOp, divOp, modOp);

        initEntry(ExpresionBasica, get(OperadorUnario));
        initEntry(ExpresionBasica, get(Operando));

        initEntry(OperadorUnario, notOp, minusOp, plusOp, incrementOp, decrementOp);

        initEntry(Primitivo, boolTrue, boolFalse, intLiteral, charLiteral, reservedNull);

        initEntry(Referencia, get(Primario));
        initEntry(_RestoReferencia, get(_Encadenado));
        initEntry(_Encadenado, dot);
        initEntry(_RestoEncadenado, get(ArgsActuales));

        initEntry(Primario, reservedThis, stringLiteral, idMetVar);
        initEntry(Primario, get(LlamadaConstructor));
        initEntry(Primario, get(LlamadaMetodoEstatico));
        initEntry(Primario, get(ExpresionParentizada));
        initEntry(_RestoLlamadaMetodo, get(ArgsActuales));

        initEntry(AccesoVar, idMetVar);

        initEntry(LlamadaConstructor, reservedNew);

        initEntry(ExpresionParentizada, openParenthesis);

        initEntry(LlamadaMetodo, idMetVar);

        initEntry(LlamadaMetodoEstatico, idClase);

        initEntry(ArgsActuales, openParenthesis);

        initEntry(ListaExpsOpcional, get(ListaExps));

        initEntry(ListaExps, get(Expresion));
        initEntry(_RestoListaExps, comma);
    }

    public void initEntry(SyntacticMethod key, TokenType... firsts){
        get(key).addAll(Arrays.asList(firsts));
    }

    public void initEntry(SyntacticMethod key, List<TokenType> tokens) {
        get(key).addAll(tokens);
    }
}
