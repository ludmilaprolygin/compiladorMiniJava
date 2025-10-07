class BloquesAnidados {
    public void metodo() {
        int x = 10;
        
        // Bloque anidado
        {
            int y = 20;
            boolean flag = true;
            
            if (flag) {
                int z = 30;
                while (z > 0) {
                    z = z - 1;
                }
            }
        }
        
        // Otro bloque
        {
            int a = 5;
            int b = 10;
            int resultado = a + b;
        }
    }
}
