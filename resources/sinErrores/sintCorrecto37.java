///[SinErrores]
// 1. Clases genéricas simples
class Generica<T> {}

// 2. Clase genérica que extiende otra genérica
class SubGenerica<T> extends Generica<T> {}

// 3. Clase con campos genéricos
class TiposGenericos {
    Generica<Integer> g1;
    Generica<String> g2;
}

// 4. Métodos con parámetros genéricos
class MetodosGenericos {
    void metodo1(Generica<String> g) {}
    void metodo2(Generica<Integer> g) {}
}

// 5. Métodos con retorno genérico
class RetornosGenericos<T> {
    T metodo3(T t) { return t; }
    Generica<T> metodo4() { return new Generica<T>(); }
}

// 6. Métodos genéricos con parámetros genéricos
class MetodosGenParam {
    T metodoGenerico(T t) { return t; }
    Generica<U> metodoGenerico2() { return new Generica<U>(); }
}

// 7. Clase genérica con herencia y métodos genéricos
class SubGenericaCompleta<T> extends Generica<T> {
    Generica<U> metodo(U u) { return new Generica<U>(); }
}

// 8. métodos combinados
class Completa<T> {
    Generica<U> metodo(U u) { return new Generica<U>(); }
}

// 10. Métodos genéricos con tipo como argumento de método
class MetodoConTipo<T> {
    void metodo(Generica<T> g) {}
}

