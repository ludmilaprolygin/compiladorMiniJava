///[SinErrores]
//Author: Lauti
class A {
    int x = 10;
    void metodo(){
        var y = x + 5;
    }
}

class B extends A{
    void metodo(){
        x = x + 20;
    }
}

class Main{
    static void main(){}
}