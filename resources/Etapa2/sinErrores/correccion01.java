///[SinErrores]

class A {
    void m1() {
        Id1.m1(4).m2(5, this, 'c') = 5; //Debe andar para futuras etapas!!!
        A.b().a = 5;
    }
}