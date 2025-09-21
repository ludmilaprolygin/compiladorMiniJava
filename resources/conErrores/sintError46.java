///[Error:)|9]

class ArgumentosMalformados {
    public void metodo() {
        int x = 10;
        int y = 5;
        
        // Llamadas con argumentos malformados
        int resultado3 = calcular(x, y,);
    }
    
    public int calcular(int a, int b) {
        return a + b;
    }
}
