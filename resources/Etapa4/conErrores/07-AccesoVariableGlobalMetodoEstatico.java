///[Error:x|8]
// Variable local duplicada en el metodo main
    //Author:Franco
class A {
    int x;

    static void main() {
        var x = x;
    }
}