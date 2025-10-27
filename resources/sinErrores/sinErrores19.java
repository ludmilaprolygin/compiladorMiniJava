///[SinErrores]
//Author: Lauti
class Utilidades {
    int sumar(int a, int b) {
        return a + b;
    }

    static int restar(int a, int b) {
        return a - b;
    }
}

class Principal extends Utilidades{
    void metodo() {
        var resultado = sumar(4, 6);

        Utilidades.restar(20, 8);

        sumar(1, 2);
    }

    static void main() {
        metodo();
    }
}
