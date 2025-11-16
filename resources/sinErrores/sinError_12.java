///1234&1111&8888&exitosamente

class A{
    int x;


    void mc1(){
        debugPrint(1234);
    }

    void mc2(){
        debugPrint(1111);
    }

    void mc3(){
        debugPrint(8888);
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


