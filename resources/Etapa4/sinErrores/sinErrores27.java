///[SinErrores]
//Author: Lauti
class Taller {
    String obtenerReporte() {
        return "Reporte: Auto arreglado.";
    }
}

class Auto {
    Taller llevarAlTaller() {
        System.printSln("Llevando auto al taller...");
        return new Taller();
    }
}

class Principal {
    static void main() {
        var miAuto = new Auto();

        // Test: Encadenado sobre una llamada a método parentizada
        // 1. (miAuto.llevarAlTaller()) es el "Primario" (NodoExpresionParentizada)
        // 2. .obtenerReporte() es el "Encadenado"
        //
        // El parser debe analizar "miAuto.llevarAlTaller()" completo
        // como la expresión interna del paréntesis.
        var reporte = (miAuto.llevarAlTaller()).obtenerReporte();
    }
}