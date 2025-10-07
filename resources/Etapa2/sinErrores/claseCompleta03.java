///[SinErrores]
abstract class Prueba1 extends Prueba2 {
    private int x = 10;
    public Lista<String> lista = new Lista<>();
    private Map<String> mapa = new HashMap<A>();

    static void prueba1(int a) {
        int x = 10;
        Float y = 20;
        boolean flag = true;

        String texto = "Hola Mundo";
        Integer numero = 100;

        //List<String> lista = new ArrayList<>();
        //Map<String> mapa = new HashMap<A>();

        int a, b, c = 10;
        String str1, str2 = "Hello World";

        for (int i = 0; i < 10; i++) {
           x = x+1;
        }
    }

    private int prueba3() {
        for (Persona persona:personas) {
            x = persona.edad;
        }
    }

    public abstract void prueba2();

    private static void prueba4(Ciudad c, int a, int b) {
        c.nombre = "Madrid";
        c.getNombre();
        {
            var d = c.nombre;
            var e = c.getNombre();
        }
    }

    public void prueba5() {
        var c = this.nombre.a.b(1, 'z', 123).c;
        var d = this.getEdad().a.b.c(1);
        var e = this.getCiudad().nombre.a.b().c;
        var f = (this.getCiudad()).getNombre().a().b.c(null);
    }

    //abstract void prueba6(Float a, Float b, Float c, Matriz<T> d);
    public abstract void prueba7();
}
