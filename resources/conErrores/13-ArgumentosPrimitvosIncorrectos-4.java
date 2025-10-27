///[Error:A|10]
// Clase B no declarada
class A {

    A (int x, char y) {

    }

    void m1() {
        var obj = new A(10, "hola");
    }

    static void main() {}
}