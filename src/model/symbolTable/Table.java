package model.symbolTable;

import model.Token;

import java.util.HashMap;
import java.util.Iterator;

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
        for(Entry e : this.entrySet()) {
            s += ((Element) e.getValue().toString()) + " ";
        }
        return s;
    }

    public boolean equals(Table<Element> table) {
        boolean toReturn = table != null && this.size() == table.size();
        if(table != null && !isEmpty() && !table.isEmpty()) {
            Element thisElement, tableElement;
            Iterator<Element> tableElements = table.values().iterator();
            Iterator<Element> thisElements = this.values().iterator();
            thisElement = thisElements.next();
            tableElement = tableElements.next();
            for(int i = 0; i < this.size() && toReturn; i++) {
                toReturn = thisElement.equals(tableElement);
                if(toReturn) {
                    if(i < this.size() - 1) {
                        thisElement = thisElements.next();
                        tableElement = tableElements.next();
                    }
                }

            }
        }
        return toReturn;
    }
}
