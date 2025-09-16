package model;

import java.util.*;

import static model.TokenType.EPSILON;

public abstract class NonTerminals extends HashMap<SyntacticMethod, HashSet<TokenType>> {
    protected NonTerminals() {
        super();
        mapInit();
    }

    protected void mapInit(){
        for (SyntacticMethod syntaticMethod: SyntacticMethod.values() ) {
            put(syntaticMethod, new HashSet<TokenType>());
        }
    }

    protected void addEpsilon(SyntacticMethod... methods) {
        for(SyntacticMethod m : methods)
            initEntry(m, EPSILON);
    }

    protected void initEntry(SyntacticMethod key, TokenType... firsts){
        get(key).addAll(Arrays.asList(firsts));
    }

    protected void initEntry(SyntacticMethod key, HashSet<TokenType> tokens) {
        get(key).addAll(tokens);
    }

    public boolean containsToken(SyntacticMethod key, TokenType token) {
        HashSet<TokenType> tokens = get(key);
        if (tokens == null) {
            return false;
        }
        return tokens.contains(token);
    }
}
