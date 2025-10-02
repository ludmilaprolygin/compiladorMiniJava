///[Error:m1|9]
// Redefinir método abstracto pero mantenerlo abstracto

abstract class A {
    abstract void m1();
}

abstract class B extends A {
    abstract void m1();
}

class Init {
    static void main() {}
}
