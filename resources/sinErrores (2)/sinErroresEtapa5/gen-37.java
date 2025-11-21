///1&1&2&11&2&3&exitosamente
class A{
    void m1(){
        debugPrint(1);
    }
}
class B extends A{
    void m2(){
        debugPrint(2);
    }
}
class C extends B{
    void m1(){
        debugPrint(11);
    }
    void m3(){
        debugPrint(3);
    }
}
class Init{
    static void main()
    {
        var a = new A();
        a.m1();
        var b = new B();
        b.m1();
        b.m2();
        var c = new C();
        c.m1();
        c.m2();
        c.m3();
    }
}