///[Error:m2|4]

class A {
    void m1() { m2(new A()); }
    void m2(B p2) {}
}

class B extends A {}

class Ex {
    static void main(){}
}
