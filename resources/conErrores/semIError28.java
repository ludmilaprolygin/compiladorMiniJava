///[Error:m1|10]
// Redefinición de método final

class A {
    final void m1() {}
}

class B extends A {
    void m1() {}
}

class Init {
    static void main() {}
}
