///1&false&exitosamente

class Init{
    static void main()
    {
        m1(1, true);
    }

    static void m1(int p1, boolean p2){
        p1 = 10;
        p2 = p1 == 5;
        System.printIln(p1);
        System.printBln(p2);
    }
}