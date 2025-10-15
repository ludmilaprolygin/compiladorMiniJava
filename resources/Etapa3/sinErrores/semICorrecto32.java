///[SinErrores]

// 11. Interfaz genérica anidada - VÁLIDO
interface Resultado<T> {
    T getValor();
    boolean isExito();
}

interface Repositorio<E> {
    Resultado<E> buscar(int id);
}