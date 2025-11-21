package model.codeGeneration;

public enum Comments {
    MOVE_THIS     ("; hacer swap de this"),
    RESERVE_RETURN("; reservar lugar para el retorno");
    private final String comment;

    Comments(String c) {
        comment = c;
    }

    public String getComment() {
        return comment;
    }
}
