///49&exitosamente

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
    public int getX() {
        return x;
    }
}

class Init{
    public static void main(String[]args) {
        var a = new A();
        var b = new B();

        System.out.println(a.x);
        System.out.println(b.x);
        System.out.println(a.getX());
        System.out.println(b.getX());

    }
}