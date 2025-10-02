///[Error:m1|9]
// Redefinición con diferente cantidad de parámetros

class A {
    void m1(int x) {}
}

class B extends A {
    void m1(int x, int y) {}
}

class Init {
    static void main() {}
}
