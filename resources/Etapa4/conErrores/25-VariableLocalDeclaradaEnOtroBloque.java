///[Error:x|9]
// Variable local duplicada en el metodo m2
//Author:Franco
class A {
    void m1() {
        {
            var x = 10;
        }
        x = 10;
    }

    static void main() {}
}