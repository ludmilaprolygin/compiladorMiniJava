///[SinErrores]
//Author: Lauti
class TestAsignacion {
    static int met1() {
        var a = 1;       // NodoAsignacion: izquierda = 'a', derecha = 1
        a = 2;           // NodoAsignacion generado por expresionRecursivo
        return a;
    }
}

class Main{
    static void main(){}
}