///false&true&1&2&exitosamente

class SinError_10 {
    static void main()
    {
        m1();
    }

    static void m1(){
        var p1 = false;
        System.printBln(p1);
        p1 = !p1;
        System.printBln(p1);
        var p2 = 1;
        System.printIln(p2);
        ++p2;
        System.printIln(p2);
    }
}
