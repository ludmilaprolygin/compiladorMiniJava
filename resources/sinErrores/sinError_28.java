///5&15&exitosamente

class A {
    int x = 5;

    A() { }
}

class Init {
    static void main () {
        var a = new A();
        int i = 15;


        debugPrint(a.x);
        debugPrint(i);
    }
}
