///49&exitosamente

class A{
    int getInt(){
        return 5;
    }
}
class Init{
    public static void main(String[]args)
    {
        var a = new A();
        var x = a.getInt();
        System.out.println(x); //5
        x = (10/2);
        System.out.println(x); //5
        x = (4+a.getInt());
        System.out.println(x); //9
        x = (4+a.getInt())*6;
        System.out.println(x);
        x = ((4+a.getInt()))*(6 - (10/2));
        System.out.println(x);
    }
}