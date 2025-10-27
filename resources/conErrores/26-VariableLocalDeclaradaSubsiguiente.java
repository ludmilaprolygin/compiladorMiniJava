///[Error:x|5]
// Variable local duplicada en el metodo m2
class A {
    void m1() {
        x = 10;
        var x = 10;

    }

    static void main() {}
}