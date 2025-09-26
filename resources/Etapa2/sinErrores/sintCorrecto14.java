class ThisReferencias {
    private int valor;
    private String nombre;
    
    public ThisReferencias(int valor, String nombre) {
        this.valor = valor;
        this.nombre = nombre;
    }
    
    public void metodo() {
        // Uso de this
        int x = this.valor;
        this.valor = 10;
        
        // Llamada a método con this
        this.metodoPrivado();
        
        // Acceso a atributos
        String texto = this.nombre;
    }
    
    private void metodoPrivado() {
        return;
    }
}
