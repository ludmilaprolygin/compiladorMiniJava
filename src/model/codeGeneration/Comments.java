package model.codeGeneration;

public enum Comments {
    MOVE_THIS     ("; hacer swap de this")
    ;
    private final String comment;

    Comments(String c) {
        comment = c;
    }

    public String getComment() {
        return comment;
    }
}
