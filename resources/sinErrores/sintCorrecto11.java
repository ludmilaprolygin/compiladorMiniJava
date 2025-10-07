class ArgumentosFormales {
    public void sinArgumentos() {
        return;
    }
    
    public void unArgumento(int x) {
        return;
    }
    
    public void multiplesArgumentos(int a, boolean b, char c) {
        return;
    }
    
    public void argumentosComplejos(int x, int y, boolean flag, String texto) {
        int resultado = x + y;
        boolean condicion = flag && (x > 0);
        String mensaje = texto + " procesado";
    }
    
    public int metodoConReturn(int valor) {
        return valor * 2;
    }
}
