///[Error:return|9]
//Author:Franco
class Init{

    void m1() {
        if (10 > 4) {
            return;
        } else {
            return;
        }


        m1();
        var x = 5; // Código muerto
    }

    static void main(){}
}

