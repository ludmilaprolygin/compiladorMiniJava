///[SinErrores]
//Author: ChatGPT
class Calculadora {
    int operar(int a, int b, String op) {
        if (op == "sumar") {
            return a + b;
        } else if (op == "restar") {
            return a - b;
        } else {
            return 0;
        }
    }

    static int promedio(int x, int y, int z) {
        return (x + y + z) / 3;
    }
}

class Principal extends Calculadora {
    void metodo() {
        var resultado1 = operar(10, 5, "sumar");
        var resultado2 = operar(20, 3, "restar");

        var prom = Calculadora.promedio(4, 8, 12);
    }
}

class Ex {
    static void main() {
        var p = new Principal();
        p.metodo();
    }
}
