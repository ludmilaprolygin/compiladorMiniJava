///[Error:m2|7]
//Author: Ludmila
class A {
    int a1;
    void m1(int p1) {
        // Error: m2() devuelve B, pero se intenta acceder a un método inexistente en A
        p1 = B.m2().a1.m2();
    }
    static B m2() {
        return new B();
    }
}

class B extends A { }
