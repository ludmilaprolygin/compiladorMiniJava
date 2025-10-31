///[Error:y|11]
//Author:Franco
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