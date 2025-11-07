///[SinErrores]
//Author: Ludmila
class A {
    int a1;
    void m1(int p1) {
        p1 = (B.m2()).m2().a1;  // igual al anterior pero con paréntesis
    }
    static B m2() {
        return new B();
    }
}

class B extends A { }

class Ex {
    static void main() {
        var obj = new A();
        obj.m1(10);
    }
}
