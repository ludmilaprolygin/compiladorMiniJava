class SentenciasControl {
    public void metodo() {
        int x = 10;
        boolean flag = true;
        
        // Sentencia if
        if (x > 5) {
            x = x + 1;
        }
        
        // Sentencia if-else
        if (flag) {
            x = 100;
        } else {
            x = 200;
        }
        
        // Sentencia while
        while (x > 0) {
            x = x - 1;
        }
        
        // Sentencia return
        return;
    }
    
    public int metodoConReturn() {
        return 42;
    }
}
