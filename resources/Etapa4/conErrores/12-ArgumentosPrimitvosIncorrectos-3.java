///[Error:A|11]
// Clase B no declarada
    //Author:Franco
class A {

    A (int x, char y) {

    }

    void m1() {
        var obj = new A(true, 'a');
    }

    static void main() {}
}