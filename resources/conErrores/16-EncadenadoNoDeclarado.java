///[Error:y|11]

class A {
    int x;
}

class B {
    A a;

    void m1() {
        a.y = 10;
    }

    static void main() {}
}