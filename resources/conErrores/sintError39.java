///[Error:x|9]

class ArgumentosMalformados {
    public void metodo() {
        int x = 10;
        int y = 5;
        
        // Llamadas sin paréntesis
        int resultado5 = calcular x, y;
    }
    
    public int calcular(int a, int b) {
        return a + b;
    }
}
