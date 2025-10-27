///[Error:A|10]
// Clase B no declarada
class A {

    A (int x, char y) {

    }

    void m1() {
        var obj = new A(null, 'a');
    }

    static void main() {}
}