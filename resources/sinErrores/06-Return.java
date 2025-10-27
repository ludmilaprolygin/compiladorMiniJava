///[SinErrores]

class A {
    A m1(){
        return new A();
    }

    A m2(){
        return this;
    }

    A m3(){
        return new B();
    }
}

class B extends A {
    void m4(){
        return;
    }
}

class Init{
    static void main()
    { }
}


