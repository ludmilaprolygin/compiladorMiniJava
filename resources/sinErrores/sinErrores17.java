///[SinErrores]
//Author: Lauti
class A{
    int a;
    String b;

    public A(String s){
        a = 10;
        b = s;
    }

    void metodoA(){
        a = a + 10;
        b = "cambiar valor";
    }
}

class Main{
    static void main(){
        var obj = new A("inicial");
    }
}