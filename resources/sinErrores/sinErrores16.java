///[SinErrores]
//Author: Lauti
class A{
    int a;

    void metodoA(){

    }
}

class B extends A{
    int b;
    int c;

    public B(int a1, int b1) {
        a = a1;
        b = b1;
    }

    void metodoA(){
        c = a + b;
    }
}

class Prueba{
    static void main() {
        var objB = new B(15, 20);
    }
}