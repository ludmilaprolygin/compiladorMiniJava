///[Error:m2|12]
// Variable local duplicada en el metodo m2
//Author:Franco
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