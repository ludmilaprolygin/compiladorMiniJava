///[SinErrores]
//Author: Ludmi

class Calculadora {
    static int sumar(int a, int b) {
        return a + b;
    }
}

class Clase {
    void m1(){
        Calculadora.sumar(2, 2);
    }
}

class Ex {
    static void main(){}
}
