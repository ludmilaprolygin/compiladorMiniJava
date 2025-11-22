///10&20&10&10&exitosamente

class A {
    int x;

    public A() {
        x = 10;
    }

    public int getX() {
        return x;
    }
}

class B extends A {
    int x;

    public B() {
        x = 20;
    }
}

class Init{
    static void main() {
        var a = new A();
        var b = new B();

        System.printIln(a.x);
        System.printIln(b.x);
        System.printIln(a.getX());
        System.printIln(b.getX());

    }
}
