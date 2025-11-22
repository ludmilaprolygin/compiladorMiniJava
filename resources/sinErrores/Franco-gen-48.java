///5&exitosamente
class A{
    int entero(){
        var x = 5;
        return x;
    }
}

class Main{
    static void main(){
        var y = new A();
        var z = y.entero();
        System.printIln(z);
    }
}