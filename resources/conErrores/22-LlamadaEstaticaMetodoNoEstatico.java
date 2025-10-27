///[Error:method|9]
// Variable local duplicada en el metodo m2
class A {
    void method() {}
}

class B {
    void m1() {
        A.method();
    }

    static void main() {}
}