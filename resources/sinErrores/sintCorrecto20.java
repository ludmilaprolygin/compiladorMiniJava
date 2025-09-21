class OperadorTernario {
    public void metodo() {
        int x = 10;
        int y = 20;
        
        int maximo = x > y ? x : y;
        boolean esPositivo = x > 0 ? true : false;
        String mensaje = x > 5 ? "Mayor" : "Menor";
        
        int resultado = x > 0 ? y : 0;
    }
}
