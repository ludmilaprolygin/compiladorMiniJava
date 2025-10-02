///[SinErrores]
// Cadena de herencia compleja

class A {
    int x;
    void m1() {}
}

class B extends A {
    int y;
    void m2() {}
}

class C extends B {
    int z;
    void m3() {}
}

class D extends C {
    void m1() {}
}

class Init {
    static void main() {}
}
