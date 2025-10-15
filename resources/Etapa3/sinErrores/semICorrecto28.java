///[SinErrores]

// 1. Interfaz correctamente declarada y usada
interface Calculadora {
    int sumar(int a, int b);
    int restar(int a, int b);
}

class CalculadoraImpl implements Calculadora {
    public int sumar(int a, int b) { return a + b; }
    public int restar(int a, int b) { return a - b; }
}