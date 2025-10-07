///[SinErrores]

// 1. Interfaz genérica simple correctamente declarada
interface Caja<T> {
    void guardar(T item);
    T obtener();
}

class CajaImpl<T> implements Caja<T> {
    private T item;
    public void guardar(T item) { this.item = item; }
    public T obtener() { return item; }
}