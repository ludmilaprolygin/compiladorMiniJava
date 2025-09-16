package main;

import model.Firsts;
import model.Following;
import model.SyntacticMethod;

public class TempMain {
    public static void main(String[] args) {
        Firsts firsts = new Firsts();
        Following following = new Following(firsts);
        for(var entry : following.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

    }

}
