///[SinErrores]

// 3. Interfaz extendiendo otra interfaz
interface Figura {
    int calcularArea();
}

interface FiguraColoreada extends Figura {
    String obtenerColor();
}

class Circulo implements FiguraColoreada {
    public int calcularArea() { return 3; }
    public String obtenerColor() { return "Rojo"; }
}