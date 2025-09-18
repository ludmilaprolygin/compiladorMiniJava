///[SinErrores]
class Prueba1<T> extends Prueba2 {
    List<T> lista;
    static void prueba1(int a) {
        this.b().a = 5;
        L prueba = new L();
    }
    T prueba2() {
        return lista.get(0);
    }
    public Prueba1(int y) {
        lista = new ArrayList<>();
    }
    public Prueba1() {
        lista = new ArrayList<T>();
    }

    // a function that solves a non-linear equation with the Newton-Raphson method
    Float newtonRaphson(Float x0, Float e) {
        Float x = x0;
        Float fx = f(x);
        Float dfx = df(x);
        while (Math.abs(fx) > e) {
            x = x - fx / dfx;
            fx = f(x);
            dfx = df(x);
        }
        return x;
    }

    // a function that aproximates a function with a Taylor polynomial
    Float taylor(Float x, int n) {
        Float sum = 0;
        for (int i = 0; i < n; i=i+1) {
            sum = Math.pow(x, i) / factorial(i);
        }
        return sum;
    }

    void prueba3() {
        for (T t: lista) {
            t = (a = b);
            for (int i = 0; i < 10; i++) {
                t = (a = b);
                while (true) {
                    if (true) {
                        t = (a = b);
                    }
                }
            }
        }
    }
}
