///[Error:m2|4]

class A {
    void m1(int p1) { m2(10>0); }
    void m2(int p1) {}
}

class B extends A {}

class Ex {
    static void main() {}
}