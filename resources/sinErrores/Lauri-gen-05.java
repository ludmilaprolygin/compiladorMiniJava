///2222&exitosamente

class A{
    int b;
    int c;
    int d;
    public A(){

        b = 10;
        c = 12;
        d = b + c;
        debugPrint(d);
    }

    void mc(){
        var x = 10;
        var y = 12;
        var z = x + y;
        debugPrint(z);
    }
}


class Init{
    static void main()
    {
        var a = new A();
        a.mc();
    }
}


