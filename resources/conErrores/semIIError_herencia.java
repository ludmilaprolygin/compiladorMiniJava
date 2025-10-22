///[Error:B|7]

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