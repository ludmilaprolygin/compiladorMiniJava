///[SinErrores]
// Clase abstracta sin herencia explícita (hereda de Object)

abstract class Forma {
    abstract int area();
}

class Cuadrado extends Forma {
    int area() { return 0; }
}

class Init {
    static void main() {}
}
