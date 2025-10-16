///[Error:B|8]

class B extends A {}
class A {}

class C {
    void m1(){
        B b = new A();
    }
}