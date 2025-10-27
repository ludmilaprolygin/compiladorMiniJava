///[Error:x|11]
// Variable local duplicada en el metodo m2
class A {
    void m1() {
        var x = 10;
    }

    void m2() {
        var x = 10;
        {
            var x = 20;
        }
    }

    static void main() {}
}