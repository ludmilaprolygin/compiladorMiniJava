///[Error:a|26]

class A {
    int x;
}

class B {
}

class C {
    B b;
}

class D {
    C c;
}

class E {
    D d;
}

class F {
    E e;

    void m1() {
        e.d.c.b.a.x = 10;
    }

    static void main() {}
}