///[Error:x|8]
// Variable local duplicada en el metodo m2
class A {
    void m1() {
        {
            var x = 10;
        }
        x = 10;
    }

    static void main() {}
}