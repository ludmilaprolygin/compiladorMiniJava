///[SinErrores]
// Un programa que declara variables locales
class A {
    int j;

    void m1(int a) {
        var x = 10;
        {
            var y = 10 + 5;
            var w = 'c';
        }
        var z = "Hello";
    }

    static void main() {}
}