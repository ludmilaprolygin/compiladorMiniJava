///[SinErrores]
// Author: Cami
// Probando encadenados.

class A {
    B a1;

    A a2;

    void m3(){}
}

class B extends A{
    A a3;

    A m2(){ return new A();}
    void m1(B p1)
    {
        a1.a3.a2 = new A();
        a1.a3.a1.a1.a1;
        a1.m2();

        m2().a2.m3();
    }
}

class Init {
    static void main() {    }
}