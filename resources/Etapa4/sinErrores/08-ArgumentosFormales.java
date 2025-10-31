///[SinErrores]

class A {
    void m1(A a) {

    }
}

class B extends A {

}

class C extends B {

}

class Init{
    static void main()
    {
        var a = new A();

        a.m1(new A());
        a.m1(new C());
        a.m1(new B());
    }
}


