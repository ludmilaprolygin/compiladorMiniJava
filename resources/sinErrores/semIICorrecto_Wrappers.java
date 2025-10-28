///[SinErrores]
// Prueba el uso correcto de las clases Wrapper.
//Author: Ludmi + Gemini

class TestWrappers {
    static void main() {

        // --- Integer ---
        Integer i;
        i = new Integer(10);   // 1. Probar Instanciación
        int val_i;
        val_i = i.intValue();    // 2. Probar llamada a método

        int suma;
        suma = 100 + i.intValue(); // 3. Probar uso en expresión

        int field_i;
        field_i = i.value;     // 4. Probar acceso a campo

        // --- Boolean ---
        Boolean b;
        b = new Boolean(true); // 1. Probar Instanciación
        boolean val_b;
        val_b = b.booleanValue(); // 2. Probar llamada a método

        // 3. Probar uso en control de flujo
        if (b.booleanValue()) {
            suma = 0;
        }

        // --- Character ---
        Character c;
        c = new Character('z');  // 1. Probar Instanciación
        char val_c;
        val_c = c.charValue();   // 2. Probar llamada a método

        // --- Null (son clases) ---
        Integer i2 = null; // 5. Probar nulabilidad
    }
}