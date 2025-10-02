///[Error:m1|9]
// Redefinición con tipos de parámetros diferentes

class A {
    void m1(int x) {}
}

class B extends A {
    void m1(boolean x) {}
}

class Init {
    static void main() {}
}
