///1234&10&88&exitosamente

class A{
    int x;


    void mc1(){
        debugPrint(1234);
    }

    void mc2(){
        debugPrint(10);
    }

    void mc3(){
        debugPrint(88);
    }
}


class Init{
    static void main()
    {
        var a = new A();
        a.mc1();
        a.mc2();
        a.mc3();
    }
}


