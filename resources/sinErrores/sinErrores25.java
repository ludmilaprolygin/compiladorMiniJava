///[SinErrores]
//Author: Lauti
class Persona {
    String nombre = "Ana";
    String obtenerNombre() {
        return nombre;
    }
}

class Fabrica {
    // Método estático que devuelve un tipo clase (Persona)
    static Persona crearPersona() {
        return new Persona();
    }
}

class Principal {
    static void main() {
        // Test: Encadenado sobre una llamada a método estático
        // El parser debe enganchar ".obtenerNombre()"
        // al NodoLlamadaMetodoEstatico de "Fabrica.crearPersona()"
        var n = Fabrica.crearPersona().obtenerNombre();
    }
}