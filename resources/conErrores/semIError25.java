///[Error:B|8]
// Clase concreta hereda método abstracto sin implementarlo

abstract class A {
    abstract void m1();
}

class B extends A {
}

class Init {
    static void main() {}
}
