package model.codeGeneration;

public enum Comments {
    MOVE_THIS        ("; hacer swap de this"),
    RESERVE_RETURN   ("; reservar lugar para el retorno"),
    FP_REGISTER      ("; apilar valor de FP"),
    SP_REGISTER      ("; apilar valor de SP"),
    STORE_STACK_TOP  ("; almacenar FP en el tope de la pila"),
    RESTORE_FP       ("; restaurar FP"),
    RESERVE_VARS     ("; reservar espacio para variables locales"),
    FREE_VARS        ("; liberar espacio de variables locales"),
    SAVE_RETURN_VALUE("; guardar valor de retorno"),;

    private final String comment;

    Comments(String c) {
        comment = c;
    }

    public String getComment() {
        return comment;
    }
    public String getComment(int n){
        return comment + "(" + n + ")";
    }
}
