///5&a&true&5&A&exitosamente

class A {
    int i = 5;
    char c = 'a';
    boolean b = true;

    int iSuma = 2 + 3;
    char cTernario = b == true ? 'A' : 'B';

    A() { }
}

class Init {
    static void main () {
        var a = new A();

        System.printIln(a.i);
        System.printCln(a.c);
        System.printBln(a.b);
        System.printIln(a.iSuma);
        System.printCln(a.cTernario);
    }
}
