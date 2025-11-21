///33&exitosamente

class A{
    void m1(){
        debugPrint(3);
    }
}

class C extends A{
    void m3(){
        debugPrint(3);
    }
}

class Init{

    static void main()
    {
        var a = new C();
        a.m1();
        a.m3();
    }
}


