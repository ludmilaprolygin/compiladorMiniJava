///[SinErrores]
// Métodos static y final válidos

class A {
    static void m1() {}
    final void m2() {}
    void m3() {}
}

class B extends A {
    void m3() {}
}

class Init {
    static void main() {}
}
