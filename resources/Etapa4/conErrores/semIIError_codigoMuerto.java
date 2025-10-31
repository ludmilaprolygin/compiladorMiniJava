///[Error:return|9]

class CodigoMuerto {
    static void main( ) {
        var x = 10;
        if (x > 5) {
            return;
        } else {
            return;
        }
        // Código muerto: la siguiente línea nunca se ejecutará
        if (x < 0) {
            x = 10;
        }
    }
}