///[SinErrores]

class Ternario {
    // Campo global con inicialización ternaria
    int x = a ? b : c;

    // Con literales
    boolean flag = (n > 0) ? true : false;

    // Mezclado con operaciones
    int z = (a + b) > 10 ? 1 : 0;

    void test() {
        // Para ver que no rompe la lógica local
        var local = cond ? 100 : 200;
    }
}


