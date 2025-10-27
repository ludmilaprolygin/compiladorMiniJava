///[SinErrores]
//Author: Lauti
class Contador {
    int valor;

    void reset() {
        valor = 0;
    }

    void incrementar() {
        valor = valor + 1;
    }

    void hacerTodo() {
        this.incrementar();
        this.reset();
    }
}

class Principal {
    static void main() {
        var c = new Contador();
        c.hacerTodo();
    }
}