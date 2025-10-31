///[SinErrores]
//Author: Lauti
class Persona {
    int edad = 30;
    String nombre = "Juan";

    String obtenerNombre() {
        return nombre;
    }
}

class Principal {
    static void main() {
        var p = new Persona();

        // Test 1: Encadenado de atributo sobre (variable)
        // El parser debe enganchar ".edad" al NodoExpresionParentizada de "(p)"
        var e = (p).edad;

        // Test 2: Encadenado de método sobre (constructor)
        // (Prueba combinada y más compleja)
        var n = (new Persona()).obtenerNombre();
    }
}