///[SinErrores]
// Redefinición válida de métodos

class A {
    void m1() {}
    int m2(char c) { return 1; }
}

class B extends A {
    void m1() {}
    int m2(char c) { return 2; }
}

class Init {
    static void main() {}
}
