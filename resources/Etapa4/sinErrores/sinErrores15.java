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

    void metodoA(){
        c = a + b;
    }
}

class Prueba{
    static void main() {
        var objB = new B();
    }
}