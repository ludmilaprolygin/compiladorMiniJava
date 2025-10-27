///[SinErrores]

class A {
    int x;
}

class B extends A {
    int y;

    void m1(int z) {
        x = 10;
        y = 20;
        z = 30;
    }

    static void main() {}
}