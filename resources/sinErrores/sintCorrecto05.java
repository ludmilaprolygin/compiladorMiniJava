///[SinErrores]
interface Calculadora {
    public int sumar(int a, int b);
    public int restar(int a, int b);
}

abstract class OperacionBase {
    int resultado;
    
    public abstract void ejecutar();
    
    public int getResultado() {
        return resultado;
    }
}

class CalculadoraImpl extends OperacionBase {
    private int valor1;
    private int valor2;
    
    public CalculadoraImpl(int v1, int v2) {
        valor1 = v1;
        valor2 = v2;
    }
    
    public int sumar(int a, int b) {
        return a + b;
    }
    
    public int restar(int a, int b) {
        return a - b;
    }
    
    public void ejecutar() {
        int resultado = valor1 > valor2 ? sumar(valor1, valor2) : restar(valor1, valor2);
    }
    
    public void procesar() {
        boolean esPositivo = valor1 > 0 && valor2 > 0;
        if (esPositivo) {
            resultado = valor1 * valor2;
        } else {
            resultado = 0;
        }
    }
}
