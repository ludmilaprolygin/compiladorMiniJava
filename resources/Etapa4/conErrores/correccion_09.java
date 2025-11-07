///[Error:=|6]

class A {}
class B extends A {
    void m1(B p1) {
        p1 = 4;
    }
}

class Ex {
    static void main(){}
}
