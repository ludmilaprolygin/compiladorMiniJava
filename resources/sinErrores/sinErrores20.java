///[SinErrores]
//Author: Lauti
class Persona {
    int edad;
    String nombre;

    public Persona(int e, String n) {
        edad = e;
        nombre = n;
    }

    int obtenerEdad() {
        return edad;
    }

    String obtenerNombre() {
        return nombre;
    }

    void saludar() {
        System.printSln("Hola!");
    }
}

class Principal {
    static void main() {
        var p = new Persona(25, "Juan");

        // Encadenado de variables
        var e = p.edad;

        // Encadenado de llamada a método
        var n = p.obtenerNombre();

        // Encadenado de varias llamadas
        var a = p.obtenerNombre();

        // Uso de this como primario
        p.saludar(); // válido solo si estamos dentro de un método de instancia
    }
}
