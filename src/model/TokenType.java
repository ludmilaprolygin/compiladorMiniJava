package model;

// https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html

public enum TokenType {
    END_OF_FILE     ("end of file"),

    idClase         ("idClase"),
    idMetVar        ("idMetVar"),

    intLiteral      ("intLiteral"),
    charLiteral     ("charLiteral"),
    stringLiteral   ("stringLiteral"),
    boolTrue        ("true"),
    boolFalse       ("false"),

    reservedNull    ("null"),
    reservedClass   ("class"),
    reservedExtends ("extends"),
    reservedPublic  ("public"),
    reservedStatic  ("static"),
    reservedVoid    ("void"),
    reservedBoolean ("boolean"),
    reservedChar    ("char"),
    reservedInt     ("int"),
    reservedAbstract("abstract"),
    reservedFinal   ("final"),
    reservedIf      ("if"),
    reservedElse    ("else"),
    reservedWhile   ("while"),
    reservedReturn  ("return"),
    reservedVar     ("var"),
    reservedThis    ("this"),
    reservedNew     ("new"),

    greaterOp       ("> greaterOp"),
    lesserOp        ("< lesserOp"),
    notOp           ("! notOp"),
    assignOp        ("= assignOp"),

    equalsOp        ("== equalsOp"),
    greaterEqualOp  (">= greaterEqualOp"),
    lesserEqualOp   ("<= lesserEqualOp"),
    notEqualOp      ("!= notEqualOp"),

    andOp           ("&& andOp"),
    orOp            ("|| orOp"),

    modOp           ("% modOp"),
    divOp           ("/ divOp"),
    plusOp          ("+ plusOp"),
    minusOp         ("- minusOp"),
    multOp          ("* multOp"),

    incrementOp     ("++ incOp"),
    decrementOp     ("-- decOp"),

    openParenthesis ("( openParenthesis"),
    closeParenthesis(") closeParenthesis"),
    openBracket     ("{ openBracket"),
    closeBracket    ("} closeBracket"),
    semicolon       ("; semicolon"),
    comma           (", comma"),
    dot             (". dot"),
    colon           (": colon"),

    quote           ("\" quote"),
    apostrophe      ("' apostrophe"),

    // Agregados para opcional de la Etapa 2
    questionMark    ("? questionMark"),
    reservedImplements ("implements"),
    reservedFor     ("for"),
    reservedPrivate ("private"),
    EPSILON("epsilon / void"),
    reservedInterface ("interface"),
    ;

    private final String typeExplanation;

    TokenType(String typeExplanation) {
        this.typeExplanation = typeExplanation;
    }

    public String getTypeExplanation() {
        return typeExplanation;
    }

    public static TokenType fromExplanation(String explanation) {
        for (TokenType tokenType : TokenType.values()) {
            if (tokenType.getTypeExplanation().equals(explanation)) {
                return tokenType;
            }
        }
        return null;
    }

}