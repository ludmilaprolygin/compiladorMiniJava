interface MiInterfaz {
    public void metodoInterfaz();
    int getNumero();
}

class Implementacion implements MiInterfaz {
    public void metodoInterfaz() {
        return;
    }
    
    public int getNumero() {
        return 42;
    }
}
