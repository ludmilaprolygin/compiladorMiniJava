///[Error:A|7]
//Author: Ludmi
class B extends A {}
class A {}
class C {
    void m1(){
        B b = new A();
    }
}

class Test {
    static void main() {}
}