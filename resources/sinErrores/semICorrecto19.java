///[SinErrores]
// Implementación de método abstracto con final

abstract class Animal {
    abstract void hacerSonido();
}

class Perro extends Animal {
    final void hacerSonido() {}
}

class Init {
    static void main() {}
}
