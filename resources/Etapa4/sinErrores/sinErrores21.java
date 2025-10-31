///[SinErrores]
//Author: Lauti
class Persona {
    int edad;
    String nombre;

    public Persona(int edad, String nombre) {
        this.edad = edad;
        this.nombre = nombre;
    }

    int obtenerEdad() {
        return edad;
    }

    String obtenerNombre() {
        return nombre;
    }

    void saludar() {
        System.printSln("Hola, mundo");
    }

    Persona duplicarEdad() {
        return new Persona(edad * 2, nombre);
    }
}

class Principal {
    static void main() {
        // Crear un objeto Persona
        var p = new Persona(25, "Juan");

        // Acceso a variable de instancia
        var e = p.edad;

        // Llamada a método
        var n = p.obtenerNombre();

        // Encadenado de llamadas a métodos
        var nueva = p.duplicarEdad().obtenerNombre();

        // Encadenado de variable + método
        var mensaje = p.nombre;

        // Uso de 'this' en un método de instancia (si estuviéramos en uno)
        // var f = this.saludar(); // válido solo dentro de método de instancia
    }
}
