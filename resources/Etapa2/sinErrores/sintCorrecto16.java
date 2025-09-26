class ConstructoresLlamadas {
    public void metodo() {
        // Llamada a constructor sin argumentos
        MiClase obj1 = new MiClase();
        
        // Llamada a constructor con argumentos
        MiClase obj2 = new MiClase(10, "test");
        
        // Llamada a constructor genérico
        ClaseGenerica<String> gen = new ClaseGenerica<String>();
        
        // Acceso a atributos
        int valor = obj1.atributo;
        obj1.atributo = 20;
        
        // Llamada a método
        obj1.metodo();
    }
}

class MiClase {
    public int atributo;
    private String nombre;
    
    public MiClase() {
        atributo = 0;
        nombre = "default";
    }
    
    public MiClase(int a, String n) {
        atributo = a;
        nombre = n;
    }
    
    public void metodo() {
        return;
    }
}
