///[Error:x|7]
// Variable local duplicada en el metodo m2
//Author:Franco
class A {
    void m2() {
        var x = null;
        var x = 5;
    }

    static void main() {}
}