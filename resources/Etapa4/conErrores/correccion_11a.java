///[Error:combinar|20]
//Author: ChatGPT
class Operaciones {
    int combinar(int a, int b, String etiqueta) {
        return a + b;
    }

    static int mezclar(int x, int y, boolean positivo) {
        if (positivo) {
            return x + y;
        } else {
            return x - y;
        }
    }
}

class Principal extends Operaciones {
    void metodo() {
        // Error 1: tipo incompatible (String en lugar de int)
        var r1 = combinar("10", 5, "Suma");

        // Error 2: parámetros en orden incorrecto (int, boolean, double)
        var r2 = Operaciones.mezclar('3', false, 4);
    }
}

class Ex {
    static void main() {
        var p = new Principal();
        p.metodo();
    }
}
