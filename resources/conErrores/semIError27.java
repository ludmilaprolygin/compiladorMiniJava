///[Error:m1|10]
// Redefinición de método estático

class A {
    static void m1() {}
}

class B extends A {
    void m1() {}
}

class Init {
    static void main() {}
}
