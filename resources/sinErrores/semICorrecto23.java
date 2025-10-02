///[SinErrores]
// Clase abstracta con métodos concretos y abstractos

abstract class A {
    void m1() {}
    abstract void m2();
}

abstract class B extends A {
    void m3() {}
}

class C extends B {
    void m2() {}
}

class Init {
    static void main() {}
}
