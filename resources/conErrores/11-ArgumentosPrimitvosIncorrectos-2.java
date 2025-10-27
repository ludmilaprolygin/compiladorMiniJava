///[Error:A|10]
// Clase B no declarada
class A {

    A (boolean x, char y) {

    }

    void m1() {
        var obj = new A(10, 10);
    }

    static void main() {}
}