package model;

import static model.SyntacticMethod.*;
import static model.TokenType.*;

public final class Following extends NonTerminals {
    private Firsts firsts;
    public Following(Firsts firsts) {
        super();
        this.firsts = firsts;
        followingInit();
    }

    private void followingInit(){

    }
}
