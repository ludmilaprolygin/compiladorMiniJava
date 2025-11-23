package model.codeGeneration;

public enum Comments {
    MOVE_THIS           ("; hacer swap de this"),
    RESERVE_RETURN      ("; reservar lugar para el retorno"),
    FP_REGISTER         ("; apilar valor de FP"),
    SP_REGISTER         ("; apilar valor de SP"),
    STORE_STACK_TOP     ("; almacenar FP en el tope de la pila"),
    RESTORE_FP          ("; restaurar FP"),
    RESERVE_VARS        ("; reservar espacio para variables locales"),
    FREE_VARS           ("; liberar espacio de variables locales"),
    SAVE_RETURN_VALUE   ("; guardar valor de retorno"),
    PROLOGUE_INIT       ("; --- INICIO PROLOGO ---"),
    EPILOGUE_INIT       ("; --- INICIO EPILOGO ---"),
    PROLOGUE_END        ("; --- FIN PROLOGO ---"),
    EPILOGUE_END        ("; --- FIN EPILOGO ---"),
    LOAD_VTABLE         ("; cargar la VT"),
    DUP_THIS            ("; duplicar this"),
    LOAD_METHOD         ("; cargar metodo"),
    FREE_RETURN_VALUE   ("; liberar valor de retorno"),
    NULL                ("; valor null"),
    ATTRIBUTE_ACCESS    ("; acceso a atributo"),
    ATTRIBUTE_ASSIGNMENT("; asignacion a atributo"),
    STORE_PARAM         ("; almacenar parametro"),
    LOAD_PARAM          ("; cargar parametro"),
    STORE_LOCAL         ("; almacenar variable local"),
    LOAD_LOCAL          ("; cargar variable local"),
    ;


    private final String comment;

    Comments(String c) {
        comment = c;
    }

    public String getComment() {
        return comment;
    }
    public String getComment(int n){
        return comment + " (" + n + ")";
    }
    public String getComment(String s) { return comment + " (" + s + ")"; }
}
