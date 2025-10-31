///[SinErrores]
//Author: Lauti
class Persona {
    String nombre;

    public Persona(String n) {
        nombre = n;
    }

    String obtenerNombre() {
        return nombre;
    }
}

class Principal {
    static void main() {
        // Test: Encadenado sobre una llamada a constructor
        // El parser debe enganchar ".obtenerNombre()"
        // al NodoLlamadaConstructor de "new Persona(...)"
        var n = new Persona("Juan").obtenerNombre();
    }
}