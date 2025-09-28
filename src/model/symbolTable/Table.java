package model.symbolTable;

import model.Token;

import java.util.HashMap;

public class Table<Element> extends HashMap<Token, Element> {
    public boolean contains(String name) {
        for(Token key : this.keySet()) {
            if(key.getLexeme().equals(name))
                return true;
        }
        return false;
    }

    public Token getTokenByName(String name) {
        for(Token key : this.keySet()) {
            if(key.getLexeme().equals(name))
                return key;
        }
        return null;
    }

    public String toString() {
        String s = "";
        for(Token key : this.keySet()) {
            s += key.getLexeme() + " ";
        }
        return s;
    }
}
