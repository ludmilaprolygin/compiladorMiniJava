///[Error:m2|11]
// Variable local duplicada en el metodo m2
class A {
    static B method() {
        return null;
    }
}

class B {
    void m1() {
        A.method().m2();
    }

    //void m2() {}

    static void main() {}
}