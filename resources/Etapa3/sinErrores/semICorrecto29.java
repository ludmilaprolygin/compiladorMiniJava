///[SinErrores]

// 2. Interfaz usada como tipo de parámetro
interface Imprimible {
    void imprimir();
}

class Documento implements Imprimible {
    public void imprimir() { }
}

class Impresora {
    void procesar(Imprimible doc) {
        doc.imprimir();
    }
}