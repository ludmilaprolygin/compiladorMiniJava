///[SinErrores]
// Casos de prueba CORRECTOS para el operador ternario.

class Init {
    static void main () {
        int a;
        int b;
        var x1 = true ? 1 : 2;
        var x2 = (1 < 0) ? true : false;
        var x3 = true ? 'a' : 'b';
        var x4 = (a > b) ? 'z' : 'y';
    }
}