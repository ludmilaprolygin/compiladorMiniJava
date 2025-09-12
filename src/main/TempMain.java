package main;

import model.Firsts;
import model.SyntacticMethod;

public class TempMain {
    public static void main(String[] args) {
        Firsts firsts = new Firsts();
        for(var entry : firsts.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

    }

}
