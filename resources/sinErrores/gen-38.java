///49&exitosamente

class A{
    int getInt(){
        return 5;
    }
}
class Init{
    static void main()
    {
        var a = new A();
        var x = (4+a.getInt())*6 - (10/2); //  (9*6) - 5 = 54 - 5 = 49
        debugPrint(x);
    }
}