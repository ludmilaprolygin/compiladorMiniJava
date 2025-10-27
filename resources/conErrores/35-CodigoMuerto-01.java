///[Error:return|9]

class Init{

    void m1() {
        var a = 10;

        a = 10 + 5;
        return;


        m1();
        var x = 5; // Código muerto
    }

    static void main(){}
}

