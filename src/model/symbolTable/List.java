package model.symbolTable;

import model.Token;
import java.util.LinkedList;

public class List extends LinkedList<Element> {
    public List() { super(); }

    public boolean contains(String name) {
        for(Element e : this) {
            if(e.getName().getLexeme().equals(name))
                return true;
        }
        return false;
    }

    public Token getTokenByName(String name) {
        for(Element e : this) {
            if(e.getName().getLexeme().equals(name))
                return e.getName();
        }
        return null;
    }

    public String toString() {
        String s = "";
        for(Element e : this) {
            s += e.toString() + " ";
        }
        return s;
    }

    public boolean equals(List list) {
        boolean toReturn = list != null && this.size() == list.size();
        if(list != null && !isEmpty() && !list.isEmpty()) {
            Element thisElement = getFirst();
            Element listElement = list.getFirst();
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

    public void put(){

    }
}
