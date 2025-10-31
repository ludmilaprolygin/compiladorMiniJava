///[Error:x|28]
//Author:Franco
class A {
    int x;
}

class B {
    A a;
    int y;
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
        e.d.c.b.y.x = 10;
    }

    static void main() {}
}