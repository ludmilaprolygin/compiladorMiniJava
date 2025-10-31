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
}

class Principal {
    static void main() {
        var p = new Persona(30, "Ana");
    }
}