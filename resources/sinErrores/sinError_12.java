///1234&1111&exitosamente

class A{
    int x;


    void mc1(){
        debugPrint(1234);
    }

    void mc2(){
        debugPrint(1111);
    }
}


class Init{
    static void main()
    {
        var a = new A();
        a.mc1();
        a.mc2();
    }
}


