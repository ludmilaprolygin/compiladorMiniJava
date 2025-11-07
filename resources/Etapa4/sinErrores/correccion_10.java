///[SinErrores]

class A {
    int p1;
    A m2() { return new A(); }
    void m3(){
        m2().p1 = 5;
    }
}

class Ex{
    static void main(){
        A a1 = new A();
        a1.m2().p1 = 5;
    }
}
