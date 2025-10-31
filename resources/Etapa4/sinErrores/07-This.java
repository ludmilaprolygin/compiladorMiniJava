///[SinErrores]

class A {
    int x;
    A a1;

    void m1(){
        this.x = 5;
        m2(this);
        m2(a1);
        m2(this.a1);
    }

    void m2(A other) {

    }

}

class Init{
    static void main()
    { }
}


