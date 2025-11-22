///1234&33&15&exitosamente
class A{
    int x;
    int y;

    void mc(){
        System.printIln(1234);
        x = 33;
        y = 15;
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


