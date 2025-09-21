///[Error:10|5]

class LlamadaMetodoSinParentesis {
    public void metodo() {
        int resultado = calcular 10, 5;
    }
    
    public int calcular(int a, int b) {
        return a + b;
    }
}
