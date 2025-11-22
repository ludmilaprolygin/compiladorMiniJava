///1234&33&15&exitosamente
class A{
    int x;
    int y;

    public A(int a, int b){
        x = a;
        y = b;
    }

    void mc(){
        System.printIln(1234);
    }
}


class Init{
    static void main()
    {
        var a = new A(33, 15);
        a.mc();
        System.printIln(a.x);
        System.printIln(a.y);
    }
}


