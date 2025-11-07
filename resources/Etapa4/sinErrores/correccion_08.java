///[SinErrores]

class A { }
class B extends A {}
class C extends B {}

class Ex {
    static void main(){
        A a = new A();
        B b = new B();
        C c = new C();

        if(a == a){};
        if(a == b){};
        if(a == c){};
        if(b == b){};
        if(b == c){};
        if(c == c){};
    }
}
