class LlamadasMetodo {
    public void metodo() {
        int x = 10;
        
        // Llamada a método sin argumentos
        metodoVacio();
        
        // Llamada a método con argumentos
        int resultado = calcular(x, 5);
        
        // Llamada a método estático
        int estatico = ClaseEstatica.metodoEstatico();
        
        // Llamada encadenada
        String texto = obtenerTexto().toUpperCase();
    }
    
    public void metodoVacio() {
        return;
    }
    
    public int calcular(int a, int b) {
        return a + b;
    }
    
    public String obtenerTexto() {
        return "Hola";
    }
}

class ClaseEstatica {
    public static int metodoEstatico() {
        return 42;
    }
}
