///1234&33&15&exitosamente
class A{
    int x;
    int y;

    public A(){
        x = 33;
        y = 15;
    }

    void mc(){
        System.printIln(1234);
    }
}


class Init{
    static void main()
    {
        var a = new A();
        a.mc();
        System.printIln(a.x);
        System.printIln(a.y);
    }
}


