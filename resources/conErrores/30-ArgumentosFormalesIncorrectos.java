///[Error:m1|22]

class A {
    void m1(B a) {

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
    }
}

