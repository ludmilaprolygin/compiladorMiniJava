package model.symbolTable;

import model.Token;
import java.util.LinkedList;

public class List extends LinkedList<OffsetElement> {
    public List() { super(); }

    public boolean contains(String name) {
        for(OffsetElement e : this) {
            if(e.getName().getLexeme().equals(name))
                return true;
        }
        return false;
    }

    public Token getTokenByName(String name) {
        for(OffsetElement e : this) {
            if(e.getName().getLexeme().equals(name))
                return e.getName();
        }
        return null;
    }

    public String toString() {
        String s = "";
        for(OffsetElement e : this) {
            s += e.toString() + " ";
        }
        return s;
    }

    public boolean equals(List list) {
        boolean toReturn = list != null && this.size() == list.size();
        if(list != null && !isEmpty() && !list.isEmpty()) {
            OffsetElement thisElement = getFirst();
            OffsetElement listElement = list.getFirst();
            for(int i = 0; i < this.size() && toReturn; i++) {
                toReturn = thisElement.equals(listElement);
                if(toReturn) {
                    if(i < this.size() - 1) {
                        thisElement = this.get(i + 1);
                        listElement = list.get(i + 1);
                    }
                }

            }
        }
        return toReturn;
    }

    public void put(Token t, OffsetElement e){
        this.addLast(e);
    }

    public OffsetElement get(Token t){
        if(t != null)
            for(OffsetElement e : this) {
                if(e != null && e.getName() != null && e.getName().getLexeme().equals(t.getLexeme()))
                    return e;
            }
        return null;
    }

    public OffsetElement[] values() {
        OffsetElement[] elements = new OffsetElement[this.size()];
        for(int i = 0; i < this.size(); i++) {
            elements[i] = this.get(i);
        }
        return elements;
    }
}
