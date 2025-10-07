///[SinErrores]
// Múltiples parámetros del mismo tipo

class A {
    void m1(int a, int b, int c) {}
    void m2(A x, A y) {}
    void m3(boolean p1, char p2, int p3) {}
}

class Init {
    static void main() {}
}
